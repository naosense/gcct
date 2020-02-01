package io.github.pingao777.jdk8.g1.model;

import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/17.
 */
public class G1Remark extends ResultData {
    private double startup;
    private double finalizeMark;
    private double refProc;
    private double uploading;
    private double duration;
    private double user;
    private double sys;
    private double real;

    public G1Remark() {
        super(Phrase.G1_REMARK);
    }

    public double getStartup() {
        return startup;
    }

    public void setStartup(double startup) {
        this.startup = startup;
    }

    public double getFinalizeMark() {
        return finalizeMark;
    }

    public void setFinalizeMark(double finalizeMark) {
        this.finalizeMark = finalizeMark;
    }

    public double getRefProc() {
        return refProc;
    }

    public void setRefProc(double refProc) {
        this.refProc = refProc;
    }

    public double getUploading() {
        return uploading;
    }

    public void setUploading(double uploading) {
        this.uploading = uploading;
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
        return "G1Remark{" +
            "startup=" + startup +
            ", finalizeMark=" + finalizeMark +
            ", refProc=" + refProc +
            ", uploading=" + uploading +
            ", user=" + user +
            ", sys=" + sys +
            ", real=" + real +
            '}';
    }
}
