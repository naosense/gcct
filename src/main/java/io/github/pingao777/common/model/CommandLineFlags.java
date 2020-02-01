package io.github.pingao777.common.model;

import io.github.pingao777.common.enums.Phrase;


/**
 * Created by pingao on 2020/1/15.
 */
public class CommandLineFlags extends ResultData {
    private String flags;
    public CommandLineFlags() {
        super(Phrase.COMMAND_LINE_FLAGS);
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    @Override
    public String toString() {
        return "CommandLineFlags{" +
            "flags='" + flags + '\'' +
            '}';
    }
}
