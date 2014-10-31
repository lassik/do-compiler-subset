import java.io.*;

/**
 * Entry point to the do2java program, which compiles a Do source file
 * into a Java source file.
 */
public class CmdLineProgDo2Java implements CmdLineProg.Runnable {

    public static void main(String[] args) throws Exception {
        new CmdLineProg("do2java", ".do", ".java", args, new CmdLineProgDo2Java()); }

    public void run(String srcPath, String dstPath) throws Exception {
        Prog prog = new ProgReader(new ConsReader(new TokReader(new FileReader(srcPath)))).read();
        new JavaWriter(new FileWriter(dstPath)).write(prog); } }
