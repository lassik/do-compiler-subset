public class TokSemi extends Tok {

    public String toString() {
        return(";");
    }

    public boolean equals(Tok t) {
        return(t instanceof TokSemi);
    }
}
