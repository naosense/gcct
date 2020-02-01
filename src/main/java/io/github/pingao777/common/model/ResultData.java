package io.github.pingao777.common.model;

import io.github.pingao777.common.enums.Phrase;


/**
 * Created by pingao on 2020/1/7.
 */
public class ResultData {
    private Phrase phrase;

    public ResultData(Phrase phrase) {
        this.phrase = phrase;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }
}
