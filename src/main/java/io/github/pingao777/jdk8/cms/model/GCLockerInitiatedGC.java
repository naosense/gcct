package io.github.pingao777.jdk8.cms.model;

import java.util.Date;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/11.
 */
public class GCLockerInitiatedGC extends ResultData {
    private Date date;
    private double startup;
    private double youngBefore;
    private double youngAfter;
    private double youngTotal;
    private double cleanUpDuration;
    private double heapBefore;
    private double heapAfter;
    private double heapTotal;
    private double duration;
    private double user;
    private double sys;
    private double real;

    public GCLockerInitiatedGC() {
        super(Phrase.CMS_GCLOCKER_INITIATED_GC);
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

    public double getYoungBefore() {
        return youngBefore;
    }

    public void setYoungBefore(double youngBefore) {
        this.youngBefore = youngBefore;
    }

    public double getYoungAfter() {
        return youngAfter;
    }

    public void setYoungAfter(double youngAfter) {
        this.youngAfter = youngAfter;
    }

    public double getYoungTotal() {
        return youngTotal;
    }

    public void setYoungTotal(double youngTotal) {
        this.youngTotal = youngTotal;
    }

    public double getCleanUpDuration() {
        return cleanUpDuration;
    }

    public void setCleanUpDuration(double cleanUpDuration) {
        this.cleanUpDuration = cleanUpDuration;
    }

    public double getHeapBefore() {
        return heapBefore;
    }

    public void setHeapBefore(double heapBefore) {
        this.heapBefore = heapBefore;
    }

    public double getHeapAfter() {
        return heapAfter;
    }

    public void setHeapAfter(double heapAfter) {
        this.heapAfter = heapAfter;
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
        return "GCLockerInitiatedGC{" +
            "timestamp=" + date +
            ", startUpTime=" + startup +
            ", youngBefore=" + youngBefore +
            ", youngAfter=" + youngAfter +
            ", youngTotal=" + youngTotal +
            ", cleanUpDuration=" + cleanUpDuration +
            ", heapBefore=" + heapBefore +
            ", heapAfter=" + heapAfter +
            ", heapTotal=" + heapTotal +
            ", heapDuration=" + duration +
            ", userTime=" + user +
            ", sysTime=" + sys +
            ", realTime=" + real +
            '}';
    }
}
