public class TokStr extends Tok {
    public String value; public TokStr(String value) { this.value = value; }
    public String toString() { return('"' + value + '"'); }
    public boolean equals(Tok t) { return((t instanceof TokStr) && value.equals(((TokStr)t).value)); } }
