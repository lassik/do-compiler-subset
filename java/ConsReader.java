import java.io.*;
import java.util.*;

/**
 * Reads Do language constructs given a stream of Do language tokens.
 */
public class ConsReader {

    private TokReader tr;

    interface R { Cons read() throws Exception; }

    private Cons readOneOf(R... rs) throws Exception {
        Cons c; for(R r: rs) if((c = r.read()) != null) return(c);
        throw new SyntaxError(); }

    class StrR implements R {
        public Cons read() throws Exception {
            return(ConsFunOpPushStr.from(tr.read(TokStr.class))); } }

    class IntR implements R {
        public Cons read() throws Exception {
            return(ConsFunOpPushInt.from(tr.read(TokInt.class))); } }

    class RefR implements R {
        public Cons read() throws Exception {
            return(ConsFunOpRef.from(tr.read(TokSym.class))); } }

    class ObjR implements R {
        public Cons read() throws Exception {
            return(readOneOf(new StrR(), new IntR(), new RefR())); } }

    class LetR implements R {
        public Cons read() throws Exception {
            if(!tr.skip(new TokSym("let"))) return(null);
            List<String> vars = new ArrayList<String>();
            while(!tr.skip(new TokSemi())) vars.add(((TokSym)tr.read(TokSym.class)).name);
            return(new ConsLet(vars)); } }

    class FunR implements R {
        public Cons read() throws Exception {
            TokSym sym = ((TokSym)tr.read(TokSym.class)); if(sym == null) return(null);
            String name = sym.name;
            List<ConsFunOp> ops = new ArrayList<ConsFunOp>();
            while(!tr.skip(new TokSemi())) ops.add((ConsFunOp)(new ObjR().read()));
            return(new ConsFun(name, ops)); } }

    class TopR implements R {
        public Cons read() throws Exception {
            if(tr.atEOF()) return(null); return(readOneOf(new LetR(), new FunR())); } }

    public Cons read() throws Exception {
        return(new TopR().read()); }

    public ConsReader(TokReader tr) {
        this.tr = tr; } }
