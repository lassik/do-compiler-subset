import java.io.*;
import java.util.*;

/**
 * Writes out a binary JVM class file corresponding to the given Do
 * program.  The class is used by giving an OutputStream to the
 * constructor.  The file is written to that stream.  Then the write
 * method is called with the program to write.
 */
public class ClassWriter {

    /*
     * Parameters from the user of the class
     */

    private OutputStream os;
    private Prog prog;

    /*
     * Temporary buffers for data to be written to the file.  We need
     * to do complicated buffering because at the beginning of the
     * class file is a constant pool whose contents are referenced at
     * later stages in the file.  This class is written such that it
     * populates the constant pool as it comes across things that need
     * to go there while writing out stuff that appears later in the
     * file.  So it would have to write in two places in the same file
     * at once.  That's why we write into temporary byte buffers that
     * are concatenated to form the final file wher we're done.  The
     * alternative would be to traverse the Do program in two passes,
     * first looking for stuff that goes in the constant pool and on
     * the second pass writing out the file.  This alternative would
     * be more complicated to code.  Buffering uses up memory but
     * that's not an issue since Do source files are tiny.
     */

    private ByteArrayOutputStream pool = new ByteArrayOutputStream();
    private ByteArrayOutputStream main = new ByteArrayOutputStream();
    private ByteArrayOutputStream file = new ByteArrayOutputStream();

    private void checkRange(int x, int min, int max) throws Exception {
        if((x < min) || (x > max)) throw new Exception(String.format("Value %d outside valid range %d..%d", x, min, max));
    }

    /*
     * Append unsigned (uN) and signed (sN) integers into the given
     * byte buffer.  The integer will take up N bytes.  It will be in
     * big endian byte order as specified for the class file format.
     */

    private void u1(ByteArrayOutputStream baos, int x) throws Exception {
        checkRange(x, 0, 255);
        baos.write(x);
    }

    private void u2(ByteArrayOutputStream baos, int x) throws Exception {
        checkRange(x, 0, 65535);
        u1(baos, 255 & (x >> 8));
        u1(baos, 255 & (x >> 0));
    }

    private void u4(ByteArrayOutputStream baos, int x) throws Exception {
        checkRange(x, 0, 2147483647);  // u4 max value doesn't fit in a java int, so this is java int's max value
        u1(baos, 255 & (x >> 24));
        u1(baos, 255 & (x >> 16));
        u1(baos, 255 & (x >> 8));
        u1(baos, 255 & (x >> 0));
    }

    private void s2(ByteArrayOutputStream baos, int x) throws Exception {
        checkRange(x, -32768, 32767);
        u1(baos, 255 & (x >> 8));
        u1(baos, 255 & (x >> 0));
    }

    /*
     * Populate the constant pool.  Each routine adds an item to the
     * pool and returns its index.
     */

    private int poolCount;

    private int constUTF8(String s) throws Exception {
        u1(pool, 1);
        byte[] b = s.getBytes("utf8");
        u2(pool, b.length);
        pool.write(b, 0, b.length);
        return(++poolCount);
    }

    private int constLiteralInt(int x) throws Exception {
        u1(pool, 3);
        u4(pool, x);
        return(++poolCount);
    }

    private int constClass(String className) throws Exception {
        int i = constUTF8(className);
        u1(pool, 7);
        u2(pool, i);
        return(++poolCount);
    }

    private int constLiteralString(String s) throws Exception {
        int i = constUTF8(s);
        u1(pool, 8);
        u2(pool, i);
        return(++poolCount);
    }

    private int constField(String className, String fieldName, String fieldType) throws Exception {
        int i = constClass(className);
        int j = constNameAndType(fieldName, fieldType);

        u1(pool, 9);
        u2(pool, i);
        u2(pool, j);
        return(++poolCount);
    }

    private int constMethod(String className, String fieldName, String fieldType) throws Exception {
        int i = constClass(className);
        int j = constNameAndType(fieldName, fieldType);
        u1(pool, 10);
        u2(pool, i);
        u2(pool, j);
        return(++poolCount);
    }

    private int constNameAndType(String fieldName, String fieldType) throws Exception {
        int i = constUTF8(fieldName);
        int j = constUTF8(fieldType);
        u1(pool, 12);
        u2(pool, i);
        u2(pool, j);
        return(++poolCount);
    }

    /*
     * Populate the main area of the class file: fields and methods
     * for the class.  Each field is declared public static and has
     * the type Object.
     */

    private void mainFields() throws Exception {
        u2(main, prog.vars.length);
        for(ProgVar var: prog.vars) {
            u2(main, 1 | 8);  // public static
            u2(main, constUTF8(var.id));
            u2(main, constUTF8("Ljava/lang/Object;"));
            u2(main, 0);  // attribute count
        }
    }

    /*
     * This turns the code in the body of a Do function into JVM
     * bytecode.  I had the most fun writing this part.  Error
     * messages from the JVM in case of malformed code were
     * surprisingly good, but there were a couple of puzzles along the
     * way.
     *
     * Basically, Java bytecode is a sequence of one-byte opcodes,
     * each followed by zero or more operands.  The opcode is a magic
     * number specifying which instruction to execute.  The count,
     * size and meaning of the operands depends on the instruction.
     */

    private byte[] methodCode(ProgFun fun) throws Exception {
        ByteArrayOutputStream code = new ByteArrayOutputStream();
        for(ProgFunOp op: fun.ops) {
            if(op instanceof ProgFunOpPrim) {
                String s = prog.prims[op.arg];
                if(s.equals("...")) {
                    // goto offset
                    int rel = -code.size();
                    u1(code, 167);
                    s2(code, rel);
                } else if(s.equals("&")) {
                    // getstatic: push DoPrim.flag onto the operand stack
                    u1(code, 178);
                    u2(code, constField("DoPrim", "flag", "Z"));
                    // ifne offset:sint16: pop operand stack, goto offset if true
                    u1(code, 154);
                    s2(code, 4);
                    // return
                    u1(code, 177);
                    // ifne jumps here
                } else if(s.equals("|")) {
                    // getstatic: push DoPrim.flag onto the operand stack
                    u1(code, 178);
                    u2(code, constField("DoPrim", "flag", "Z"));
                    // ifeq offset:sint16: pop operand stack, goto offset if false
                    u1(code, 153);
                    s2(code, 4);
                    // return
                    u1(code, 177);
                    // ifeq jumps here
                } else {
                    // invokestatic index:uint16 -- const pool symbolic ref to method
                    u1(code, 184);
                    u2(code, constMethod("DoPrim", DoPrimID.get(s), "()V"));
                }
            } else if(op instanceof ProgFunOpCall) {
                // invokestatic index:uint16 -- const pool symbolic ref to method
                u1(code, 184);
                u2(code, constMethod("DoProgram", prog.funs[op.arg].id, "()V"));
            } else if(op instanceof ProgFunOpPushInt) {
                // ldc_w index:uint16 -> pushed
                u1(code, 19);
                u2(code, constLiteralInt(op.arg));
                // invokestatic index:uint16 -- const pool symbolic ref to method
                u1(code, 184);
                u2(code, constMethod("DoPrim", "push", "(I)V"));
            } else if(op instanceof ProgFunOpPushStr) {
                // ldc_w index:uint16 -> pushed
                u1(code, 19);
                u2(code, constLiteralString(prog.strs[op.arg]));
                // invokestatic index:uint16 -- const pool symbolic ref to method
                u1(code, 184);
                u2(code, constMethod("DoPrim", "push", "(Ljava/lang/Object;)V"));
            } else if(op instanceof ProgFunOpVarGet) {
                // getstatic index:uint16 -> value pushed onto operand stack / index is const pool index
                u1(code, 178);
                u2(code, constField("DoProgram", prog.vars[op.arg].id, "Ljava/lang/Object;"));
                // invokestatic index:uint16 -- const pool symbolic ref to method
                u1(code, 184);
                u2(code, constMethod("DoPrim", "push", "(Ljava/lang/Object;)V"));
            } else if(op instanceof ProgFunOpVarSet) {
                // invokestatic index:uint16 -- const pool symbolic ref to method
                u1(code, 184);
                u2(code, constMethod("DoPrim", "pop", "()Ljava/lang/Object;"));
                // putstatic index:uint16 -- const pool symbolic ref to field
                u1(code, 179);
                u2(code, constField("DoProgram", prog.vars[op.arg].id, "Ljava/lang/Object;"));
            }
        }
        u1(code, 177); // return
        return(code.toByteArray());
    }

    /*
     * Writes out the declaration and code for a single Java method,
     * which corresponds to a single Do function.
     *
     * emitted methods will have the signature: public static void myMethod() throws Exception
     * except that main() will have its usual String[] arg
     */

    private void mainMethods() throws Exception {
        u2(main, prog.funs.length);
        for(ProgFun fun: prog.funs) {
            boolean isMain = fun.id.equals("_main0");
            u2(main, 1 | 8);  // public static
            u2(main, constUTF8(isMain ? "main" : fun.id));
            u2(main, constUTF8(isMain ? "([Ljava/lang/String;)V" : "()V"));  // no args, void return value
            u2(main, 2);  // attribute count
            // exceptions attribute
            u2(main, constUTF8("Exceptions"));
            u4(main, 4);  // how many bytes follow
            u2(main, 1);  // exception count
            u2(main, constClass("Ljava/lang/Exception;"));
            // code attribute
            byte[] code = methodCode(fun);
            u2(main, constUTF8("Code"));
            u4(main, 12 + code.length);  // how many bytes follow
            u2(main, 1);  // operand stack depth
            u2(main, isMain ? 1 : 0);  // local variable count, incl. args
            u4(main, code.length);
            main.write(code, 0, code.length);
            u2(main, 0);  // catch block count
            u2(main, 0);  // attribute count
        }
    }

    /*
     * Calls other methods to generate all the difficult bits of the
     * class file into byte buffers, then writes out the class file.
     */

    public void write(Prog prog) throws Exception {
        int thisClass  = constClass("DoProgram");
        int superClass = constClass("java/lang/Object");
        this.prog = prog;
        mainFields();
        mainMethods();
        u2(file, 0xcafe);  // best magic
        u2(file, 0xbabe);  // ever!
        u2(file, 0);  // major version
        u2(file, 50); // minor version
        u2(file, poolCount + 1);
        pool.writeTo(file);
        u2(file, 1); // public class
        u2(file, thisClass);
        u2(file, superClass);
        u2(file, 0); // interface count
        main.writeTo(file); // fields and methods
        u2(file, 0); // attribute count
        file.writeTo(os);
    }

    public ClassWriter(OutputStream os) {
        this.os = os;
    }
}
