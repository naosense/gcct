package io.github.pingao777.common.parser;

/**
 * Created by pingao on 2020/1/14.
 */
public class ParseContext {
    private String path;
    private double start;
    private double end;

    public ParseContext() {
        start = 0d;
        end = Double.POSITIVE_INFINITY;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }
}
