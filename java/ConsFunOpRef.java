public class ConsFunOpRef extends ConsFunOp {

    public String name; public ConsFunOpRef(String name) {
        this.name = name;
    }

    public String toString() {
        return("ref " + name);
    }

    public static ConsFunOpRef from(Tok t) {
        if(t == null) return(null); return(new ConsFunOpRef(((TokSym)t).name));
    }
}

