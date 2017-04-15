package ru.merkulyevsasha.yat.data.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class Trans {

    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("json")
    @Expose
    private String json;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
