package tux.pt.radio.helper;

public class TimerHelper {
    private long time;

    public TimerHelper() {
        this.time = System.currentTimeMillis();
    }

    public boolean passed(double delay) {
        return System.currentTimeMillis() - this.time >= delay;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }
}
