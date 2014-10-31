import java.util.*;

/**
 * The DoPrim class contains the runtime support common to all Do
 * programs.  See the discussion on Do to Java translation for
 * details.
 */
public class DoPrimID {
    
    private static Map<String, String> map = new HashMap<String, String>();
    
    private static void initialize() {
        if(!map.isEmpty()) return;
        // Data primitives
        map.put("?"           , "p_s_ask");
        map.put("not"         , "p_not");
        map.put("drop"        , "p_drop");
        map.put("none"        , "p_none");
        map.put("true"        , "p_true");
        map.put("false"       , "p_false");
        // Comparison primitives
        map.put("="           , "p_s_eq");
        map.put("<>"          , "p_s_ne");
        map.put("<="          , "p_s_le");
        map.put("<"           , "p_s_lt");
        map.put(">="          , "p_s_ge");
        map.put(">"           , "p_s_gt");
        // Arithmetic primitives
        map.put("+"           , "p_s_add");
        map.put("-"           , "p_s_sub");
        map.put("*"           , "p_s_mul");
        map.put("/mod"        , "p_s_divmod");
        // Sequence primitives
        map.put("vector"      , "p_vector");
        map.put("count"       , "p_count");
        map.put("find"        , "p_find");
        map.put("push"        , "p_push");
        map.put("item"        , "p_item");
        map.put("item!"       , "p_s_itemstore");
        // I/O primitives
        map.put("readline"    , "p_readline");
        map.put("newline"     , "p_newline");
        map.put("write"       , "p_write");
        map.put("showstack"   , "p_showstack");
        map.put("die"         , "p_die");
        // Cell phone primitives implemented in DoPrimMIDlet.
        // Couldn't test these so they're disabled.
        //map.put("newform"     , "p_newform");
        //map.put("newintfield" , "p_newintfield");
        //map.put("newokcmd"    , "p_newokcmd");
        //map.put("newbackcmd"  , "p_newbackcmd");
        //map.put("addcmd"      , "p_addcmd");
        //map.put("addctl"      , "p_addctl");
        //map.put("curform"     , "p_curform");
        //map.put("curform!"    , "p_s_curformstore");
        //map.put("fieldint"    , "p_fieldint");
        //map.put("clearform"   , "p_clearform");
        //map.put("exit"        , "p_exit");
        //map.put("randint<"    , "p_s_randintlt");
        //map.put("intstr"      , "p_intstr");
        //map.put("is"          , "p_is");
    }
    
    public static String get(String name) throws Exception {
        initialize(); String id = map.get(name);
        if(id == null) throw new Exception("No primitive named " + name);
        return(id);
    }
}
