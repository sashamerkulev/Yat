package ru.merkulyevsasha.yat.data.db;

import java.util.List;

import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.pojo.Word;


/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public interface DbDataSource {


    int saveHistory(Trans trans, String translatedText);

    int getFavorite(int id);
    void setFavorite(int id, boolean favorite);

    List<Word> getHistory();
    void deleteHistory();
    List<Word> getFavorites();
    void deleteFavorites();

    List<Word> searchHistory(String text);
    List<Word> searchFavorites(String text);
}
