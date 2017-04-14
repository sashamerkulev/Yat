package ru.merkulyevsasha.yat.domain;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ExecutorService;

import ru.merkulyevsasha.yat.data.YatRepository;
import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.pojo.Def;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class YatInteractorImpl implements YatInteractor {

    private YatRepository repo;
    private ExecutorService executor;

    public YatInteractorImpl(ExecutorService executor, YatRepository repo){
        this.repo = repo;
        this.executor = executor;
    }

    @Override
    public void setFavorite(final int id, final YatFavoriteChangedCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean favorite = !repo.getFavorite(id);
                    repo.setFavorite(id, favorite);
                    callback.success(favorite);
                } catch(Exception e){
                    callback.failure(new YatInteractorException(e));
                }
            }
        });
    }

    @Override
    public void loadHistory(final YatLoadCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{

                    List<Word> items = repo.getHistory();
                    callback.success(items);

                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }

    @Override
    public void deleteHistory(final YatDeleteCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    repo.deleteHistory();
                    callback.success();
                } catch(Exception e){
                    callback.failure(new YatInteractorException(e));
                }
            }
        });
    }

    @Override
    public void loadFavorites(final YatLoadCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{

                    List<Word> items = repo.getFavorites();
                    callback.success(items);

                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }

    @Override
    public void deleteFavorites(final YatDeleteCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    repo.deleteFavorites();
                    callback.success();
                } catch(Exception e){
                    callback.failure(new YatInteractorException(e));
                }
            }
        });
    }

    @Override
    public void translate(final String text, final String language, final String ui, final YatTranslateCallback callback) {

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Trans result = repo.translate(text, language, ui);
                    if (result == null) {
                        callback.failure(new YatInteractorException());
                        return;
                    }
                    Gson gson = new Gson();
                    Word word = gson.fromJson(result.getJson(), Word.class);
                    List<Def> def = word.getDef();
                    if (def.size() > 0){
                        String translatedText = def.get(0).getTr().get(0).getText();
                        int id = repo.saveHistory(result, translatedText);

                        word.setText(text);
                        word.setTranslatedText(translatedText);
                        word.setLanguage(language);
                        word.setFavorite(repo.getFavorite(id));
                        word.setId(id);
                        callback.success(word);

                    } else {
                        callback.failure(new YatInteractorException());
                    }
                } catch(Exception e){
                    callback.failure(new YatInteractorException(e));
                }
            }
        });

    }
}
