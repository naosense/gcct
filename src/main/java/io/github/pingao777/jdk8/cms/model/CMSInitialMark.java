package io.github.pingao777.jdk8.cms.model;

import java.util.Date;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/11.
 */
public class CMSInitialMark extends ResultData {

    private Date date;
    private double startup;
    private double oldOccupy;
    private double oldAvailable;
    private double heapOccupy;
    private double heapTotal;
    private double duration;
    private double user;
    private double sys;
    private double real;

    public CMSInitialMark() {
        super(Phrase.CMS_INITIAL_MARK);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getStartup() {
        return startup;
    }

    public void setStartup(double startup) {
        this.startup = startup;
    }

    public double getOldOccupy() {
        return oldOccupy;
    }

    public void setOldOccupy(double oldOccupy) {
        this.oldOccupy = oldOccupy;
    }

    public double getOldAvailable() {
        return oldAvailable;
    }

    public void setOldAvailable(double oldAvailable) {
        this.oldAvailable = oldAvailable;
    }

    public double getHeapOccupy() {
        return heapOccupy;
    }

    public void setHeapOccupy(double heapOccupy) {
        this.heapOccupy = heapOccupy;
    }

    public double getHeapTotal() {
        return heapTotal;
    }

    public void setHeapTotal(double heapTotal) {
        this.heapTotal = heapTotal;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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
        return "CMSInitialMark{" +
            "timestamp=" + date +
            ", startUpTime=" + startup +
            ", oldOccupy=" + oldOccupy +
            ", oldAvailable=" + oldAvailable +
            ", heapOccupy=" + heapOccupy +
            ", heapTotal=" + heapTotal +
            ", markDuration=" + duration +
            ", userTime=" + user +
            ", sysTime=" + sys +
            ", realTime=" + real +
            '}';
    }
}
