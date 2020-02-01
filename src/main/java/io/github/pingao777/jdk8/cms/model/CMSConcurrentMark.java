package io.github.pingao777.jdk8.cms.model;

import java.util.Date;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/11.
 */
public class CMSConcurrentMark extends ResultData {
    private Date timestamp;
    private double startup;
    private double duration;
    private double clock;
    private double user;
    private double sys;
    private double real;

    public CMSConcurrentMark() {
        super(Phrase.CMS_CONCURRENT_MARK);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public double getClock() {
        return clock;
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public double getUser() {
        return user;
    }

    public void setUser(double user) {
        this.user = user;
    }

    public double getSys() {
        return sys;
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    @Override
    public String toString() {
        return "CMSConcurrentMark{" +
            "timestamp=" + timestamp +
            ", startUpTime=" + startup +
            ", duration=" + duration +
            ", clock=" + clock +
            ", userTime=" + user +
            ", sysTime=" + sys +
            ", realTime=" + real +
            '}';
    }
}
