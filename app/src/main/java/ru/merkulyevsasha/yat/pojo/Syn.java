package ru.merkulyevsasha.yat.pojo;

import java.io.Serializable;

/**
 * Created by sasha_merkulev on 12.04.2017.
 */

public class Syn implements Serializable {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
