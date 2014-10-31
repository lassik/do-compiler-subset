public class ConsFunOpPushStr extends ConsFunOp {
    public String value; public ConsFunOpPushStr(String value) { this.value = value; }
    public String toString() { return("push \"" + value + "\""); }
    public static ConsFunOpPushStr from(Tok t) {
        if(t == null) return(null); return(new ConsFunOpPushStr(((TokStr)t).value)); } }
