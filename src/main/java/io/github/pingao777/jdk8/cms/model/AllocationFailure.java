package io.github.pingao777.jdk8.cms.model;

import java.util.Date;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/11.
 */
public class AllocationFailure extends ResultData {
//3.807: [GC (Allocation Failure) : 3.807: [ParNew: 65753K->65753K(78656K), 0.0000169 secs]: 3.807: [CMS: 162561K->174783K(174784K), 0.4932089 secs] 228315K->180964K(253440K), [Metaspace: 2138K->2138K(4480K)], 0.4933073 secs] [Times: user=0.50 sys=0.00, real=0.49 secs]

    private Date date;
    private double startup;
    private double youngBefore;
    private double youngAfter;
    private double youngTotal;
    private double youngDuration;
    private double heapBefore;
    private double heapAfter;
    private double heapTotal;
    private double duration;
    private double user;
    private double sys;
    private double real;

    public AllocationFailure() {
        super(Phrase.CMS_ALLOCATION_FAILURE);
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

    public double getYoungDuration() {
        return youngDuration;
    }

    public void setYoungDuration(double youngDuration) {
        this.youngDuration = youngDuration;
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
        return "GCAllocationFailure{" +
            "timestamp=" + date +
            ", time=" + startup +
            ", youngBefore=" + youngBefore +
            ", youngAfter=" + youngAfter +
            ", youngTotal=" + youngTotal +
            ", cleanUpDuration=" + youngDuration +
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
