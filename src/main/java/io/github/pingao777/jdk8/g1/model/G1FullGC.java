package io.github.pingao777.jdk8.g1.model;

import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/20.
 */
public class G1FullGC extends ResultData {
    private double startup;
    private double duration;
    private double edenBefore;
    private double edenAfter;
    private double edenTotalBefore;
    private double edenTotalAfter;
    private double survivorsBefore;
    private double survivorsAfter;
    private double heapBefore;
    private double heapAfter;
    private double heapTotalBefore;
    private double heapTotalAfter;
    private double user;
    private double sys;
    private double real;
    public G1FullGC() {
        super(Phrase.G1_FULL_GC);
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

    public double getEdenBefore() {
        return edenBefore;
    }

    public void setEdenBefore(double edenBefore) {
        this.edenBefore = edenBefore;
    }

    public double getEdenAfter() {
        return edenAfter;
    }

    public void setEdenAfter(double edenAfter) {
        this.edenAfter = edenAfter;
    }

    public double getEdenTotalBefore() {
        return edenTotalBefore;
    }

    public void setEdenTotalBefore(double edenTotalBefore) {
        this.edenTotalBefore = edenTotalBefore;
    }

    public double getEdenTotalAfter() {
        return edenTotalAfter;
    }

    public void setEdenTotalAfter(double edenTotalAfter) {
        this.edenTotalAfter = edenTotalAfter;
    }

    public double getSurvivorsBefore() {
        return survivorsBefore;
    }

    public void setSurvivorsBefore(double survivorsBefore) {
        this.survivorsBefore = survivorsBefore;
    }

    public double getSurvivorsAfter() {
        return survivorsAfter;
    }

    public void setSurvivorsAfter(double survivorsAfter) {
        this.survivorsAfter = survivorsAfter;
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

    public double getHeapTotalBefore() {
        return heapTotalBefore;
    }

    public void setHeapTotalBefore(double heapTotalBefore) {
        this.heapTotalBefore = heapTotalBefore;
    }

    public double getHeapTotalAfter() {
        return heapTotalAfter;
    }

    public void setHeapTotalAfter(double heapTotalAfter) {
        this.heapTotalAfter = heapTotalAfter;
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
        return "G1FullGC{" +
            "startup=" + startup +
            ", duration=" + duration +
            ", edenBefore=" + edenBefore +
            ", edenAfter=" + edenAfter +
            ", edenTotalBefore=" + edenTotalBefore +
            ", edenTotalAfter=" + edenTotalAfter +
            ", survivorsBefore=" + survivorsBefore +
            ", survivorsAfter=" + survivorsAfter +
            ", heapBefore=" + heapBefore +
            ", heapAfter=" + heapAfter +
            ", heapTotalBefore=" + heapTotalBefore +
            ", heapTotalAfter=" + heapTotalAfter +
            ", user=" + user +
            ", sys=" + sys +
            ", real=" + real +
            '}';
    }
}
