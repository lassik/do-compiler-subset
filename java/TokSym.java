public class TokSym extends Tok {

    public String name;

    public TokSym(String name) {
        this.name = name;
    }

    public String toString() {
        return(name);
    }

    public boolean equals(Tok t) {
        return((t instanceof TokSym) && name.equals(((TokSym)t).name));
    }
}
