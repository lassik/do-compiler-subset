import java.util.*;

public class ProgFun {

    public String name;
    public String id;
    public ProgFunOp[] ops;

    public ProgFun(String name, String id, List<ProgFunOp> ops) {
        this.name = name; this.id = id; this.ops = ops.toArray(new ProgFunOp[] {});
    }

    private String p(Object[] xs) {
        StringBuilder sb = new StringBuilder(); sb.append("[ ");
        for(Object x: xs) {
            sb.append(x); sb.append(" ");
        }
        sb.append("]"); return(sb.toString());
    }

    public String toString() {
        return("fun " + id + " " + p(ops));
    }
}
