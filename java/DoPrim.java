import java.io.*;
import java.util.*;

/**
 * The DoPrim class contains the runtime support common to all Do
 * programs.  See the discussion on Do to Java translation for
 * details.
 */
public class DoPrim {

    public static boolean flag;
    public static ArrayList stack = new ArrayList();
    public static Scanner scanner = new Scanner(System.in);

    // Internal helpers

    public static Object peek(int i) {
        return(stack.get(stack.size() - 1 - i));
    }

    public static void drop(int n) {
        stack.subList(stack.size() - n, stack.size()).clear();
    }

    public static void push(Object x) {
        stack.add(x);
    }

    public static void push(int x) {
        push(new Integer(x));
    }

    public static void droppush(int n, Object x) {
        drop(n);
        push(x);
    }

    public static Object pop() {
        Object x = peek(0);
        drop(1);
        return(x);
    }

    // Data primitives

    public static void p_s_ask() {
        flag = !((peek(0) == null) || ((peek(0) instanceof Boolean) && ((Boolean)peek(0)).booleanValue() == false));
        drop(1);
    }

    public static void p_not() {
        flag = !flag;
    }

    public static void p_drop() {
        drop(1);
    }

    public static void p_none() {
        push(null);
    }

    public static void p_true() {
        push(new Boolean(true));
    }

    public static void p_false() {
        push(new Boolean(false));
    }

    // Comparison primitives

    public static void p_s_eq() {
        flag = (((Integer)peek(1)).intValue() == ((Integer)peek(0)).intValue());
        drop(1);
    }

    public static void p_s_ne() {
        flag = (((Integer)peek(1)).intValue() != ((Integer)peek(0)).intValue());
        drop(1);
    }

    public static void p_s_le() {
        flag = (((Integer)peek(1)).intValue() <= ((Integer)peek(0)).intValue());
        drop(1);
    }

    public static void p_s_lt() {
        flag = (((Integer)peek(1)).intValue() <  ((Integer)peek(0)).intValue());
        drop(1);
    }

    public static void p_s_ge() {
        flag = (((Integer)peek(1)).intValue() >= ((Integer)peek(0)).intValue());
        drop(1);
    }

    public static void p_s_gt() {
        flag = (((Integer)peek(1)).intValue() >  ((Integer)peek(0)).intValue());
        drop(1);
    }

    // Arithmetic primitives

    public static void p_s_add() {
        droppush(2, ((Integer)peek(1)).intValue() + ((Integer)peek(0)).intValue());
    }

    public static void p_s_sub() {
        droppush(2, ((Integer)peek(1)).intValue() - ((Integer)peek(0)).intValue());
    }

    public static void p_s_mul() {
        droppush(2, ((Integer)peek(1)).intValue() * ((Integer)peek(0)).intValue());
    }

    public static void p_s_divmod() {
        int q = ((Integer)peek(1)).intValue() / ((Integer)peek(0)).intValue();
        int r = ((Integer)peek(1)).intValue() % ((Integer)peek(0)).intValue();
        drop(2);
        push(q); 
        push(r);
    }

    // Sequence operations

    public static void p_vector() {
        ArrayList al = new ArrayList();
        int n = ((Integer)pop()).intValue();
        for(; n > 0; --n) al.add(null);
        push(al);
    }

    public static void p_count() throws Exception {
        if(peek(0) instanceof ArrayList) {
            push(new Integer(((ArrayList)pop()).size()));
        } else if(peek(0) instanceof String) {
            push(new Integer(((String)pop()).length()));
        } else {
            throw new Exception("Sequence required");
        }
    }

    public static void p_find() {
        int i = ((String)pop()).indexOf((String)pop());
        flag = (i >= 0);
        if(flag) push(new Integer(i));
    }

    public static void p_push() {
        ((ArrayList)pop()).add(pop());
    }

    public static void p_item() throws Exception {
        if(peek(0) instanceof ArrayList) {
            droppush(1, ((ArrayList)peek(0)).get(((Integer)peek(1)).intValue()));
        } else if(peek(0) instanceof String) {
            int i = ((Integer)peek(1)).intValue();
            droppush(1, ((String)peek(0)).substring(i, i + 1));
        } else {
            throw new Exception("Sequence required");
        }
    }

    public static void p_s_itemstore() {
        droppush(2, ((ArrayList)peek(0)).set(((Integer)peek(2)).intValue(), peek(1)));
    }

    // Input/output

    public static void p_readline() {
        if((flag = scanner.hasNextLine())) push(scanner.nextLine());
    }

    public static void p_newline() {
        System.out.println();
        System.out.flush();
    }

    public static void p_write() {
        System.out.print(pop());
        System.out.flush();
    }

    public static void p_showstack() {
        System.out.println(stack);
    }

    public static void p_die() {
        System.err.println((String)pop());
        System.err.flush();
        System.exit(1);
    }
}
