public class ConsFunOpPushInt extends ConsFunOp {
    public int value; public ConsFunOpPushInt(int value) { this.value = value; }
    public String toString() { return("push " + Integer.toString(value)); }
    public static ConsFunOpPushInt from(Tok t) {
        if(t == null) return(null); return(new ConsFunOpPushInt(((TokInt)t).value)); } }
