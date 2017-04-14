package ru.merkulyevsasha.yat.data;


import java.util.List;

import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public interface YatRepository {


    Trans translate(String text, String lang, String ui);
    int saveHistory(Trans trans, String translatedText);

    boolean getFavorite(int id);
    void setFavorite(int id, boolean favorite);

    List<Word> getHistory();
    List<Word> getFavorites();

}
