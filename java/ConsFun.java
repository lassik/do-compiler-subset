import java.util.*;

public class ConsFun extends Cons {
    public String name; public ConsFunOp[] ops;
    public ConsFun(String name, List<ConsFunOp> ops) { this.name = name; this.ops = ops.toArray(new ConsFunOp[] {}); }
    public String toString() { return("fun " + name); } }
