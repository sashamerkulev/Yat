package ru.merkulyevsasha.yat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sasha_merkulev on 12.04.2017.
 */

public class Ex implements Serializable {

    private String text;
    private List<TrEx> tr;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TrEx> getTrEx() {
        return tr;
    }

    public void setTrEx(List<TrEx> tr) {
        this.tr = tr;
    }
}
