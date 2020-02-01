package io.github.pingao777.jdk8.cms.model;

import java.util.Date;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/11.
 */
public class CMSFinalRemark extends ResultData {
    private Date date;
    private double startup;
    private double youngOccupy;
    private double youngTotal;
    private double rescanDuration;
    private double weakRefProcessDuration;
    private double classUploadDuration;
    private double scrubSymbolTableDuration;
    private double scrubStringTableDuration;
    private double oldOccupy;
    private double oldTotal;
    private double heapOccupy;
    private double heapTotal;
    private double duration;
    private double user;
    private double sys;
    private double real;

    public CMSFinalRemark() {
        super(Phrase.CMS_FINAL_REMARK);
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

    public double getYoungOccupy() {
        return youngOccupy;
    }

    public void setYoungOccupy(double youngOccupy) {
        this.youngOccupy = youngOccupy;
    }

    public double getYoungTotal() {
        return youngTotal;
    }

    public void setYoungTotal(double youngTotal) {
        this.youngTotal = youngTotal;
    }

    public double getRescanDuration() {
        return rescanDuration;
    }

    public void setRescanDuration(double rescanDuration) {
        this.rescanDuration = rescanDuration;
    }

    public double getWeakRefProcessDuration() {
        return weakRefProcessDuration;
    }

    public void setWeakRefProcessDuration(double weakRefProcessDuration) {
        this.weakRefProcessDuration = weakRefProcessDuration;
    }

    public double getClassUploadDuration() {
        return classUploadDuration;
    }

    public void setClassUploadDuration(double classUploadDuration) {
        this.classUploadDuration = classUploadDuration;
    }

    public double getScrubSymbolTableDuration() {
        return scrubSymbolTableDuration;
    }

    public void setScrubSymbolTableDuration(double scrubSymbolTableDuration) {
        this.scrubSymbolTableDuration = scrubSymbolTableDuration;
    }

    public double getScrubStringTableDuration() {
        return scrubStringTableDuration;
    }

    public void setScrubStringTableDuration(double scrubStringTableDuration) {
        this.scrubStringTableDuration = scrubStringTableDuration;
    }

    public double getOldOccupy() {
        return oldOccupy;
    }

    public void setOldOccupy(double oldOccupy) {
        this.oldOccupy = oldOccupy;
    }

    public double getOldTotal() {
        return oldTotal;
    }

    public void setOldTotal(double oldTotal) {
        this.oldTotal = oldTotal;
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
        return "CMSFinalRemark{" +
            "timestamp=" + date +
            ", startUpTime=" + startup +
            ", youngOccupy=" + youngOccupy +
            ", youngTotal=" + youngTotal +
            ", rescanDuration=" + rescanDuration +
            ", weakRefProcessDuration=" + weakRefProcessDuration +
            ", classUploadDuration=" + classUploadDuration +
            ", scrubSymbolTableDuration=" + scrubSymbolTableDuration +
            ", scrubStringTableDuration=" + scrubStringTableDuration +
            ", oldOccupy=" + oldOccupy +
            ", oldTotal=" + oldTotal +
            ", heapOccupy=" + heapOccupy +
            ", heapTotal=" + heapTotal +
            ", duration=" + duration +
            ", userTime=" + user +
            ", sysTime=" + sys +
            ", realTime=" + real +
            '}';
    }
}
