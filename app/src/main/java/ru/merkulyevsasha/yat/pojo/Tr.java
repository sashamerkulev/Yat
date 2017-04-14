package ru.merkulyevsasha.yat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sasha_merkulev on 12.04.2017.
 */

public class Tr implements Serializable {

    private String text;
    private String pos;
    private List<Syn> syn;
    private List<Mean> mean;
    private List<Ex> ex;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<Syn> getSyn() {
        return syn;
    }

    public void setSyn(List<Syn> syn) {
        this.syn = syn;
    }

    public List<Mean> getMean() {
        return mean;
    }

    public void setMean(List<Mean> mean) {
        this.mean = mean;
    }

    public List<Ex> getEx() {
        return ex;
    }

    public void setEx(List<Ex> ex) {
        this.ex = ex;
    }
}
