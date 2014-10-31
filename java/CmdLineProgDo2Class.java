import java.io.*;

/**
 * Entry point to the do2class program, which compiles a Do source
 * file into a binary Java class file.
 */
public class CmdLineProgDo2Class implements CmdLineProg.Runnable {

    public static void main(String[] args) throws Exception {
        new CmdLineProg("do2class", ".do", ".class", args, new CmdLineProgDo2Class()); }

    public void run(String srcPath, String dstPath) throws Exception {
        Prog prog = new ProgReader(new ConsReader(new TokReader(new FileReader(srcPath)))).read();
        new ClassWriter(new FileOutputStream(dstPath)).write(prog); } }
