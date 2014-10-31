import java.io.*;

/**
 * JavaWriter writes out a Java source code file based on a Do program
 * definition.  This is a very straightforward undertaking.
 */
public class JavaWriter {

    private Writer writer;

    private void emit(String s) throws IOException {
        writer.write(s); writer.write("\n"); writer.flush();
    }

    public void write(Prog prog) throws Exception {
        emit("public class DoProgram {");
        emit("public static void main(String[] args) throws Exception { _main0(); }");
        for(ProgVar var: prog.vars) {
            emit("static Object " + var.id + ";");
        }
        for(ProgFun fun: prog.funs) {
            boolean shouldBreak = true;
            emit("static void " + fun.id + "() throws Exception {");
            emit("for(;;) {");
            for(ProgFunOp op: fun.ops) {
                if(op instanceof ProgFunOpPrim) {
                    String s = prog.prims[op.arg];
                    if(s.equals("...")) {
                        emit("continue;");
                        shouldBreak = false;
                    } else if(s.equals("&")) {
                        emit("if(!DoPrim.flag) break;");
                    } else if(s.equals("|")) {
                        emit("if(DoPrim.flag) break;");
                    } else {
                        emit("DoPrim." + DoPrimID.get(s) + "();");
                    }
                } else if(op instanceof ProgFunOpCall) {
                    emit(prog.funs[op.arg].id + "();");
                } else if(op instanceof ProgFunOpPushInt) {
                    emit("DoPrim.push(new Integer(" + op.arg + "));");
                } else if(op instanceof ProgFunOpPushStr) {
                    emit("DoPrim.push(\"" + prog.strs[op.arg] + "\");");
                } else if(op instanceof ProgFunOpVarGet) {
                    emit("DoPrim.push(" + prog.vars[op.arg].id + ");");
                } else if(op instanceof ProgFunOpVarSet) {
                    emit(prog.vars[op.arg].id + " = DoPrim.pop();");
                }
            }
            if(shouldBreak) emit("break;");
            emit("} }");
        }
        emit("}");
    }

    public JavaWriter(Writer writer) {
        this.writer = writer;
    }
}
