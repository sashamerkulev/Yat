package ru.merkulyevsasha.yat.domain;


import java.util.List;
import java.util.concurrent.ExecutorService;

import ru.merkulyevsasha.yat.data.YatRepository;
import ru.merkulyevsasha.yat.data.pojo.Trans;
import ru.merkulyevsasha.yat.helper.GsonHelper;
import ru.merkulyevsasha.yat.pojo.Def;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 09.04.2017.
 */

public class YatInteractorImpl implements YatInteractor {

    private final YatRepository repo;
    private final ExecutorService executor;

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

    private void setWord(Word word, String text, String translatedText, String language, int id){
        word.setText(text);
        word.setTranslatedText(translatedText);
        word.setLanguage(language);
        word.setFavorite(repo.getFavorite(id));
        word.setId(id);
    }

    @Override
    public void translate(final String text, final String language, final String ui, final YatTranslateCallback callback) {

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    Word item = repo.findWord(text, language);
                    if (item == null) {
                        Trans result = repo.translate(text, language, ui);
                        if (result == null) {
                            callback.failure(new YatInteractorException());
                            return;
                        }
                        Word word = GsonHelper.json2Word(result.getJson());
                        List<Def> def = word.getDef();
                        if (def.size() > 0){
                            String translatedText = def.get(0).getTr().get(0).getText();
                            int id = repo.saveHistory(result, translatedText);
                            setWord(word, text, translatedText, language, id);
                            callback.success(word);

                        } else {
                            callback.failure(new YatInteractorException());
                        }
                    } else {
                        Word word = GsonHelper.json2Word(item.getJson());
                        List<Def> def = word.getDef();
                        setWord(word, text, def.get(0).getTr().get(0).getText(), language, (int) item.getId());
                        callback.success(word);
                    }
                } catch(Exception e){
                    callback.failure(new YatInteractorException(e));
                }
            }
        });

    }

    @Override
    public void searchHistory(final String text, final YatLoadCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{

                    List<Word> items = repo.searchHistory(text);
                    callback.success(items);

                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }

    @Override
    public void searchFavorites(final String text, final YatLoadCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{

                    List<Word> items = repo.searchFavorites(text);
                    callback.success(items);

                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }

    @Override
    public void setLanguageIndex(final int index) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    repo.setLanguageIndex(index);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLanguageIndex() {
        return repo.getLanguageIndex();
    }
}
