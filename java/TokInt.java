public class TokInt extends Tok {
    public int value; public TokInt(int value) { this.value = value; }
    public String toString() { return(Integer.toString(value)); }
    public boolean equals(Tok t) { return((t instanceof TokInt) && (value == ((TokInt)t).value)); } }
