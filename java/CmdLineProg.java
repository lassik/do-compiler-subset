/**
 * Support for writing entry points to Java programs designed to be
 * run from the command line.  The program takes one required command
 * line argument, which gives a file name.  This is the "source" file
 * name, and a corresponding "destination" file name is derived from
 * it.  The source and destination files must both have a particular
 * file name extension determined by the caller.  On the command line,
 * the source file name extension may be omitted, in which case it is
 * filled in.
 */

public class CmdLineProg {

    public interface Runnable {
        void run(String srcPath, String dstPath) throws Exception;
    }

    private String progname, srcExt, dstExt;
    private Runnable runnable;

    private void dieSaying(String s) {
        System.err.println(s);
        System.exit(1); 
    }

    private void usage() {
        dieSaying("usage: " + progname + " sourcefile"); 
    }

    private boolean isPathSep(char c) {
        return((c == '/') || (c == '\\') || (c == ':')); 
    }

    private int extStart(String s) {
        for(int i = s.length() - 1; i >= 0; --i) {
            if(isPathSep(s.charAt(i))) break;
            if(s.charAt(i) == '.') return(i); 
        }
        return(s.length()); 
    }

    private String ext(String s) {
        return(s.substring(extStart(s), s.length())); 
    }

    private String withExt(String s, String ext) {
        return(s.substring(0, extStart(s)) + ext); 
    }

    private boolean okExt(String s) {
        return(ext(s).equals("") || ext(s).equals(srcExt)); 
    }

    private void runWithArg(String s) throws Exception {
        if(!okExt(s)) usage();
        try {
            runnable.run(withExt(s, srcExt), withExt(s, dstExt));
        } catch(Exception e) {
            dieSaying(progname + ": error: " + e + " " + e.getMessage()); 
        }
    }

    public CmdLineProg(String progname, String srcExt, String dstExt, String[] args, Runnable runnable) throws Exception {
        this.progname = progname;
        this.srcExt = srcExt;
        this.dstExt = dstExt;
        this.runnable = runnable;
        if(args.length != 1) usage();
        runWithArg(args[0]); 
    } 
}
