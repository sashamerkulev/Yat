package ru.merkulyevsasha.yat.domain;

import java.util.List;

import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public interface YatInteractor {

    interface YatTranslateCallback {
        void success(Word word);
        void failure(Exception e);
    }

    interface YatLoadCallback {
        void success(List<Word> word);
        void failure(Exception e);
    }

    interface YatDeleteCallback {
        void success();
        void failure(Exception e);
    }

    void setFavorite(int id, boolean favorite);

    void loadHistory(YatLoadCallback callback);
    void deleteHistory(YatDeleteCallback callback);
    void loadFavorites(YatLoadCallback callback);
    void deleteFavorites(YatDeleteCallback callback);

    void translate(String word, String language, final String ui, YatTranslateCallback callback);

}
