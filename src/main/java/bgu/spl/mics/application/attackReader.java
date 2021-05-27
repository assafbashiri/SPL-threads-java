package bgu.spl.mics.application;

import java.util.Arrays;

public class attackReader {
    int duration;
    int [] serials;

    public attackReader(int duration, int[] serials) {
        this.duration = duration;
        this.serials = serials;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int[] getSerials() {
        return serials;
    }

    public void setSerials(int[] serials) {
        this.serials = serials;
    }

    @Override
    public String toString() {
        return "attackReader{" +
                "duration=" + duration +
                ", serials=" + Arrays.toString(serials) +
                '}';
    }
}
