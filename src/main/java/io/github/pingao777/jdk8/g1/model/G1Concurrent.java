package io.github.pingao777.jdk8.g1.model;

import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/20.
 */
public class G1Concurrent extends ResultData {
    private double startup;
    private double duration;
    public G1Concurrent() {
        super(Phrase.G1_CONCURRENT_MARK);
    }

    public double getStartup() {
        return startup;
    }

    public void setStartup(double startup) {
        this.startup = startup;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "G1Concurrent{" +
            "startup=" + startup +
            ", duration=" + duration +
            '}';
    }
}
