package ru.merkulyevsasha.yat.helper;

import com.google.gson.Gson;

import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 22.04.2017.
 */

public class GsonHelper {

    public static Word json2Word(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Word.class);
    }
}
