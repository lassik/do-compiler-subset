/**
 * The IntP class is a wrapper for an integer predicate -- that is, a
 * method that takes an integer argument and returns a boolean
 * indicating whether that argument satisfies some condition.
 */
public abstract class IntP {
    
    public abstract boolean call(int x);
    
    public static IntP oneOf(final int... theks) {
        return(new IntP() {
                private int[] ks; { this.ks = theks; }
                public boolean call(int x) {
                    for(int k: ks) if(x == k) return(true);
                    return(false); } }); }

    public static IntP notOneOf(final int... theks) {
        return(new IntP() {
                private int[] ks; { this.ks = theks; }
                public boolean call(int x) {
                    for(int k: ks) if(x == k) return(false);
                    return(true); } }); } }
