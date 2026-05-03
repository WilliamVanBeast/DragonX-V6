package dragonclient.util.killaura;

public class StopWatch {
    public long start;

    public StopWatch() {
        this.reset();
    }
    public boolean elapsed(long period) {
        return (System.currentTimeMillis()-period) > start;
    }
    public boolean elapsed2(double period) {
        return (System.currentTimeMillis()-period) > start;
    }

    public void reset() {
        start = System.currentTimeMillis();
    }
}