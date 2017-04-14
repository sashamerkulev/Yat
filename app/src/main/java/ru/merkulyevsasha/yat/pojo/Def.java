package ru.merkulyevsasha.yat.pojo;

import java.util.List;

/**
 * Created by sasha_merkulev on 12.04.2017.
 */

public class Def {

    private String text;
    private String pos;
    private List<Tr> tr;

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

    public List<Tr> getTr() {
        return tr;
    }

    public void setTr(List<Tr> tr) {
        this.tr = tr;
    }
}
