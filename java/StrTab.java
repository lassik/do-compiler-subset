import java.util.*;

/**
 * The StrTab class is a pretty trivial string table.  One gives it a
 * string; it gives back the index of the string within the table.
 */
public class StrTab {
    
    private List<String> strings = new ArrayList<String>();
    
    public int indexOfOld(String s) {
        for(int i = strings.size() - 1; i >= 0; --i)
            if(s.equals(strings.get(i)))
                return(i);
        return(-1); }

    public int indexOfNew(String s) {
        strings.add(s); return(strings.size() - 1); }

    public int indexOfOldOrNew(String s) {
        int i = indexOfOld(s); if(i == -1) i = indexOfNew(s); return(i); }

    public String[] strings() {
        return(strings.toArray(new String[] {})); } }
