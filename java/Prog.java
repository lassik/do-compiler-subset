import java.util.*;

public class Prog {

    public String[] prims;
    public String[] strs;
    public ProgVar[] vars;
    public ProgFun[] funs;

    public Prog(StrTab prims, StrTab strs, List<ProgVar> vars, List<ProgFun> funs) {
        this.prims = prims.strings();
        this.strs = strs.strings();
        this.vars = vars.toArray(new ProgVar[] {});
        this.funs = funs.toArray(new ProgFun[] {});
    }

    private String p(Object[] xs, boolean newline) {
        StringBuilder sb = new StringBuilder(); sb.append(newline? "\n" : "[ ");
        for(Object x: xs) {
            if(newline) sb.append("  "); sb.append(x); sb.append(newline ? "\n" : " ");
        }
        if(!newline) sb.append("]"); return(sb.toString());
    }

    public String toString() {
        return("prog\n" +
               " prims = " + p(prims, false) + "\n" +
               " strs  = " + p(strs, false)  + "\n" +
               " vars  = " + p(vars, false)  + "\n" +
               " funs  = " + p(funs, true)   + "\n");
    }
}
