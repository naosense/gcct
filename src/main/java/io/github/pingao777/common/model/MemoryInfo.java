package io.github.pingao777.common.model;

import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.ResultData;


/**
 * Created by pingao on 2020/1/7.
 */
public class MemoryInfo extends ResultData {
    private double page;
    private double physicalTotal;
    private double physicalFree;
    private double swapTotal;
    private double swapFree;

    public MemoryInfo() {
        super(Phrase.MEMORY_INFO);
    }

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
        this.page = page;
    }

    public double getPhysicalTotal() {
        return physicalTotal;
    }

    public void setPhysicalTotal(double physicalTotal) {
        this.physicalTotal = physicalTotal;
    }

    public double getPhysicalFree() {
        return physicalFree;
    }

    public void setPhysicalFree(double physicalFree) {
        this.physicalFree = physicalFree;
    }

    public double getSwapTotal() {
        return swapTotal;
    }

    public void setSwapTotal(double swapTotal) {
        this.swapTotal = swapTotal;
    }

    public double getSwapFree() {
        return swapFree;
    }

    public void setSwapFree(double swapFree) {
        this.swapFree = swapFree;
    }

    @Override
    public String toString() {
        return "MemoryInfo{" +
            "page='" + page + '\'' +
            ", physicalTotal='" + physicalTotal + '\'' +
            ", physicalFree='" + physicalFree + '\'' +
            ", swapTotal='" + swapTotal + '\'' +
            ", swapFree='" + swapFree + '\'' +
            '}';
    }
}
