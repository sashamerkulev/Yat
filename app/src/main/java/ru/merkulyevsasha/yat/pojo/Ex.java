package ru.merkulyevsasha.yat.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sasha_merkulev on 12.04.2017.
 */

public class Ex implements Serializable {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tr")
    @Expose
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
