import java.util.*;

/**
 * ProgReader assembles a Do program definition from Do source code
 * constructs supplied by a ConsReader.  It does name mangling and
 * differentiates source constructs denoting operations within
 * functions into their final forms.
 */
public class ProgReader {

    private ConsReader cr;
    private StrTab prims = new StrTab();
    private StrTab strs = new StrTab();
    private List<ProgVar> vars = new ArrayList<ProgVar>();
    private List<ProgFun> funs = new ArrayList<ProgFun>();
    private Map<String, ProgFunOp> dict = new HashMap<String, ProgFunOp>();
    private Map<String, Integer> nameid = new HashMap<String, Integer>();

    private String nameIDBase(String name) {
        StringBuilder sb = new StringBuilder(); sb.append('_');
        for(char c: name.toCharArray()) sb.append(Character.isLetterOrDigit(c) ? c : '_');
        return(sb.toString());
    }

    private String nameNewID(String name) {
        String base = nameIDBase(name); Integer obj = nameid.get(base);
        int i = (obj == null) ? 0 : (obj.intValue() + 1); nameid.put(base, i);
        return(base + String.format("%d", i));
    }

    private ProgFunOp compileFunOp(ConsFunOpPushStr cfo) {
        return(new ProgFunOpPushStr(strs.indexOfOldOrNew(cfo.value)));
    }

    private ProgFunOp compileFunOp(ConsFunOpPushInt cfo) {
        return(new ProgFunOpPushInt(cfo.value));
    }

    private ProgFunOp compileFunOp(ConsFunOpRef cfo) {
        ProgFunOp pfo = dict.get(cfo.name); if(pfo != null) return(pfo);
        return(new ProgFunOpPrim(prims.indexOfOldOrNew(cfo.name)));
    }

    private ProgFunOp compileFunOp(ConsFunOp cfo) {
        if(cfo instanceof ConsFunOpPushStr)
            return(compileFunOp((ConsFunOpPushStr)cfo));
        if(cfo instanceof ConsFunOpPushInt)
            return(compileFunOp((ConsFunOpPushInt)cfo));
        if(cfo instanceof ConsFunOpRef)
            return(compileFunOp((ConsFunOpRef)cfo));
        return(null);
    }

    private void compile(ConsFun cf) {
        int idx = funs.size(); List<ProgFunOp> ops = new ArrayList<ProgFunOp>();
        for(ConsFunOp cfo: cf.ops) ops.add(compileFunOp(cfo));
        funs.add(new ProgFun(cf.name, nameNewID(cf.name), ops));
        dict.put(cf.name, new ProgFunOpCall(idx));
    }

    private void compile(ConsLet let) {
        for(String varname: let.varnames) {
            int idx = vars.size();
            vars.add(new ProgVar(varname, nameNewID(varname)));
            dict.put(varname, new ProgFunOpVarGet(idx));
            dict.put(varname + "!", new ProgFunOpVarSet(idx));
        }
    }

    private void compile(Cons c) throws Exception {
        if(c instanceof ConsFun)
            compile((ConsFun)c);
        else if(c instanceof ConsLet)
            compile((ConsLet)c);
        else
            throw new SyntaxError();
    }

    public Prog read() throws Exception {
        Cons c; while((c = cr.read()) != null) compile(c);
        return(new Prog(prims, strs, vars, funs));
    }

    public ProgReader(ConsReader cr) {
        this.cr = cr;
    }
}
