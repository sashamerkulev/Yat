package ru.merkulyevsasha.yat.data;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.merkulyevsasha.yat.data.db.DbDataSource;
import ru.merkulyevsasha.yat.data.http.HttpDataSource;
import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.data.pref.YatSharedPreferences;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class YatRepositoryImpl implements YatRepository{

    private final DbDataSource db;
    private final HttpDataSource http;
    private final YatSharedPreferences pref;

    public YatRepositoryImpl(DbDataSource db, HttpDataSource http, YatSharedPreferences pref){
        this.db = db;
        this.http = http;
        this.pref = pref;
    }

    @Override
    public Word findWord(String text, String language) {
        return db.findWord(text, language);
    }

    @Override
    public Trans translate(String text, String lang, String ui) {
        Trans result = null;
        try {
            Response<ResponseBody> call =  http.translate(text, lang, ui).execute();
            if (call.isSuccessful()){
                ResponseBody body = call.body();

                String json = body.string();

                result = new Trans();
                result.setJson(json);
                result.setLang(lang);
                result.setText(text);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int saveHistory(Trans trans, String translatedText) {
        return db.saveHistory(trans, translatedText);
    }

    @Override
    public boolean getFavorite(int id) {
        return db.getFavorite(id) == 1;
    }

    @Override
    public void setFavorite(int id, boolean favorite) {
        db.setFavorite(id, favorite);
    }

    @Override
    public List<Word> getHistory() {
        return db.getHistory();
    }

    @Override
    public void deleteHistory() {
        db.deleteHistory();
    }

    @Override
    public List<Word> getFavorites() {
        return db.getFavorites();
    }

    @Override
    public void deleteFavorites() {
        db.deleteFavorites();
    }

    @Override
    public List<Word> searchHistory(String text) {
        return db.searchHistory(text);
    }

    @Override
    public List<Word> searchFavorites(String text) {
        return db.searchFavorites(text);
    }

    @Override
    public void setLanguageIndex(int index) {
        pref.setLanguageIndex(index);
    }

    @Override
    public int getLanguageIndex() {
        return pref.getLanguageIndex();
    }


}
