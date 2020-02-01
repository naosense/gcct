package io.github.pingao777.jdk8.cms.model;

import java.util.Date;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/11.
 */
public class FullGC extends ResultData {
    private Date date;
    private double startup;
    private String reason;
    private double oldBefore;
    private double oldAfter;
    private double oldTotal;
    private double oldDuration;
    private double heapBefore;
    private double heapAfter;
    private double heapTotal;
    private double metaspaceBefore;
    private double metaspaceAfter;
    private double metaspaceTotal;
    private double duration;
    private double user;
    private double sys;
    private double real;

    public FullGC() {
        super(Phrase.CMS_FULL_GC);
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getOldBefore() {
        return oldBefore;
    }

    public void setOldBefore(double oldBefore) {
        this.oldBefore = oldBefore;
    }

    public double getOldAfter() {
        return oldAfter;
    }

    public void setOldAfter(double oldAfter) {
        this.oldAfter = oldAfter;
    }

    public double getOldTotal() {
        return oldTotal;
    }

    public void setOldTotal(double oldTotal) {
        this.oldTotal = oldTotal;
    }

    public double getOldDuration() {
        return oldDuration;
    }

    public void setOldDuration(double oldDuration) {
        this.oldDuration = oldDuration;
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


    public double getMetaspaceBefore() {
        return metaspaceBefore;
    }

    public void setMetaspaceBefore(double metaspaceBefore) {
        this.metaspaceBefore = metaspaceBefore;
    }

    public double getMetaspaceAfter() {
        return metaspaceAfter;
    }

    public void setMetaspaceAfter(double metaspaceAfter) {
        this.metaspaceAfter = metaspaceAfter;
    }

    public double getMetaspaceTotal() {
        return metaspaceTotal;
    }

    public void setMetaspaceTotal(double metaspaceTotal) {
        this.metaspaceTotal = metaspaceTotal;
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
        return "FullGC{" +
            "timestamp=" + date +
            ", startUp=" + startup +
            ", reason='" + reason + '\'' +
            ", oldBefore=" + oldBefore +
            ", oldAfter=" + oldAfter +
            ", oldTotal=" + oldTotal +
            ", oldDuration=" + oldDuration +
            ", heapBefore=" + heapBefore +
            ", heapAfter=" + heapAfter +
            ", heapTotal=" + heapTotal +
            ", metaspaceBefore=" + metaspaceBefore +
            ", metaspaceAfter=" + metaspaceAfter +
            ", metaspaceTotal=" + metaspaceTotal +
            ", metaspaceDuration=" + duration +
            ", userTime=" + user +
            ", sysTime=" + sys +
            ", realTime=" + real +
            '}';
    }
}
