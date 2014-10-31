import java.util.*;

public class ConsLet extends Cons {
    public String[] varnames;
    public ConsLet(List<String> varnames) { this.varnames = varnames.toArray(new String[] {}); }
    public String toString() { return("let " + varnames.length + " vars"); } }
