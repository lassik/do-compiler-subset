public class ProgFunOpCall extends ProgFunOp {

    public ProgFunOpCall(int arg) {
        this.arg = arg;
    }

    public String toString() {
        return("call" + arg);
    }
}
