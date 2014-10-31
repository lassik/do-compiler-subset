import java.io.*;

/**
 * Stream wrapper that conditionally reads characters if they match
 * the given character class.  A character class is represented as a
 * function that returns a boolean indicating whether the given
 * character is a member of the class.  Characters are actually ints,
 * not chars, since we need to be able to represent the special values
 * EOF and NOMATCH, which are negative so they don't conflict with
 * char values.
 */
public class CondReader {

    public static final int EOF = -1;      // must match EOF return value of PushbackReader#read
    public static final int NOMATCH = -2;  // must be negative and distinct from EOF

    private PushbackReader pr;

    public int readOne(IntP p) throws IOException {
        int c = pr.read(); if(p.call(c)) return(c);
        if(c != EOF) pr.unread(c); return(NOMATCH); }

    public String readSome(IntP p) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c; while((c = readOne(p)) != NOMATCH) sb.append((char)c);
        if(sb.length() > 0) return(sb.toString()); return(null); }

    public boolean skipOne(IntP p) throws IOException {
        return(readOne(p) != NOMATCH); }

    public boolean skipOne(int x) throws IOException {
        return(skipOne(IntP.oneOf(x))); }
    
    public boolean skipSome(IntP p) throws IOException {
        if(!skipOne(p)) return(false); while(skipOne(p)); return(true); }
    
    public CondReader(PushbackReader pr) {
        this.pr = pr; }

    public CondReader(Reader r) {
        this(new PushbackReader(r)); } }
