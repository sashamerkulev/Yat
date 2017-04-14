package ru.merkulyevsasha.yat.data;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.merkulyevsasha.yat.data.db.DbDataSource;
import ru.merkulyevsasha.yat.data.http.HttpDataSource;
import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class YatRepositoryImpl implements YatRepository{

    private DbDataSource db;
    private HttpDataSource http;

    public YatRepositoryImpl(DbDataSource db, HttpDataSource http){
        this.db = db;
        this.http = http;
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


}
