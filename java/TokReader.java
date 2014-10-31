import java.io.*;

public class TokReader {

    private static IntP whiteChar = IntP.oneOf(' ', '\t', '\n');
    private static IntP lineChar  = IntP.notOneOf('\n', CondReader.EOF);
    private static IntP bareChar  = IntP.notOneOf('#', ';', '"', ' ', '\t', '\n', CondReader.EOF);
    private static IntP strChar   = IntP.notOneOf('"', '\\', '\n', CondReader.EOF);

    private CondReader cr;
    private Tok nextTok;

    private TokInt parseInt(String s, String prefix, int radix) {
        boolean isneg = s.startsWith("-");
        if(isneg) s = s.substring(1);
        if(!s.startsWith(prefix)) return(null);
        s = s.substring(prefix.length());
        int val;
        try {
            val = Integer.parseInt(s, radix);
        } catch(NumberFormatException e) {
            return(null);
        }
        if(val < 0) return(null);
        if(isneg) val = -val;
        return(new TokInt(val));
    }

    private Tok readBare() throws IOException {
        Tok r;
        String s = cr.readSome(bareChar);
        if(s == null) return(null);
        r = parseInt(s, "", 10);
        if(r != null) return(r);
        r = parseInt(s, "0x", 16);
        if(r != null) return(r);
        return(new TokSym(s));
    }

    private char readStrChar() throws IOException, SyntaxError {
        int x = cr.readOne(strChar);
        if(x == CondReader.NOMATCH) throw new SyntaxError();
        return((char)x);
    }

    private TokStr readStr() throws IOException, SyntaxError {
        if(!cr.skipOne('"')) return(null);
        StringBuilder sb = new StringBuilder();
        while(!cr.skipOne('"')) sb.append(readStrChar());
        return(new TokStr(sb.toString()));
    }

    private TokSemi readSemi() throws IOException {
        if(!cr.skipOne(';')) return(null);
        return(new TokSemi());
    }

    private boolean skipComment() throws IOException {
        if(!cr.skipOne('#')) return(false);
        cr.skipSome(lineChar);
        cr.skipOne('\n');
        return(true);
    }

    public boolean atEOF() throws IOException {
        while(cr.skipSome(whiteChar) || skipComment());
        return(cr.skipOne(CondReader.EOF));
    }

    private Tok readNext() throws IOException, SyntaxError {
        if(atEOF()) return(null);
        Tok r;
        r = readStr();
        if(r != null) return(r);
        r = readSemi();
        if(r != null) return(r);
        r = readBare();
        if(r != null) return(r);
        throw new SyntaxError();
    }

    private Tok next() throws IOException, SyntaxError {
        if(nextTok == null) nextTok = readNext();
        return(nextTok);
    }

    private Tok consumeNextIf(boolean ok) {
        if(!ok) return(null);
        Tok t = nextTok;
        nextTok = null;
        return(t);
    }

    public Tok read(Class cls) throws IOException, SyntaxError {
        return(consumeNextIf(cls.isInstance(next())));
    }

    public boolean skip(Tok t) throws IOException, SyntaxError {
        return(null != consumeNextIf(t.equals(next())));
    }

    public TokReader(Reader reader) {
        this.cr = new CondReader(reader);
    }
}
