package ru.merkulyevsasha.yat.presentation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ru.merkulyevsasha.yat.domain.YatInteractor;
import ru.merkulyevsasha.yat.pojo.Word;
import ru.merkulyevsasha.yat.presentation.pojo.HistoryState;
import ru.merkulyevsasha.yat.presentation.pojo.SettingsState;
import ru.merkulyevsasha.yat.presentation.pojo.StatePresenter;
import ru.merkulyevsasha.yat.presentation.pojo.TranslateState;

/**
 * Created by sasha_merkulev on 03.04.2017.
 */

public class YatPresenterImpl {

    public static final LinkedHashMap<String, String> LANGUAGES = new LinkedHashMap<String, String>();

    static{
        LANGUAGES.put("ru-en", "Русский -> Английский");
        LANGUAGES.put("en-ru", "Английский -> Русский");
        LANGUAGES.put("ru-fr", "Русский -> Французский");
        LANGUAGES.put("fr-ru", "Французский -> Русский");
    }

    private YatActivity view;

    private StatePresenter state;
    private YatInteractor inter;

    public YatPresenterImpl(YatInteractor inter){

        state = new StatePresenter();
        state.setFragments(StatePresenter.Fragments.Translate);
        state.setTranslateState(new TranslateState(0, "",  0));
        state.setHistoryState(new HistoryState(HistoryState.HistoryPage));
        state.setSettingsState(new SettingsState());
        this.inter = inter;
    }

    void onStart(YatActivity view){
        this.view = view;

        StatePresenter.Fragments fragment = state.getFragments();
        if (fragment == StatePresenter.Fragments.Translate) {
            onTranslateFragmentSelected();
        } else if (fragment == StatePresenter.Fragments.History){
            onHistoryFragmentSelected();
        } else if (fragment == StatePresenter.Fragments.Settings){
            onSettingsFragmentSelected();
        }

    }

    void onStop(){
        this.view = null;
    }

    void onTranslateFragmentSelected(){
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.Translate);
        TranslateState translateState = state.getTranslateState();
        view.showTranslateFragment(translateState.getSelectedLanguage(), translateState.getText(),
                translateState.getWord(), translateState.getFullscreen());
    }

    void onHistoryFragmentSelected(){
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.History);
        HistoryState historyState = state.getHistoryState();
        view.showHistoryFragment(historyState.getSelectedPage(), historyState.getSearchText());

    }

    void onSettingsFragmentSelected(){
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.Settings);
        SettingsState settingsState = state.getSettingsState();
        view.showSettingsFragment();
    }

    void onTitle1Click(){
        if (view == null)
            return;

        StatePresenter.Fragments fragment = state.getFragments();
        if (fragment == StatePresenter.Fragments.Translate){
            TranslateState translateState = state.getTranslateState();
            view.showSelectLanguageDialog(translateState.getSelectedLanguage());
            return;
        }

        if (fragment == StatePresenter.Fragments.History){
            onSelectHistoryPage();
        }
    }

    void onTitle2Click(){
        if (view == null)
            return;

        StatePresenter.Fragments fragment = state.getFragments();
        if (fragment == StatePresenter.Fragments.History){
            onSelectFavoritePage();
        }
    }

    private void translate(){
        final TranslateState translateState = state.getTranslateState();
        String text = translateState.getText();

        if (text.length() < 3)
            return;

        if (view == null)
            return;

        view.showProgress();
        String language = (String) LANGUAGES.keySet().toArray()[translateState.getSelectedLanguage()];
        inter.translate(translateState.getText(), language, "ru", new YatInteractor.YatTranslateCallback() {
            @Override
            public void success(Word word) {

                translateState.setWord(word);
                if (view == null)
                    return;
                view.hideProgress();

                view.showTranslatedText(word);
            }

            @Override
            public void failure(Exception e) {
                if (view == null)
                    return;
                view.hideProgress();

                view.showTranslateErrorMessage();
            }
        });

    }

    void onSelectLanguage(int selectedLanguage){
        TranslateState translateState = state.getTranslateState();
        translateState.setSelectedLanguage(selectedLanguage);

        if (view == null)
            return;

        translate();
    }

    void onTextTranslate(String text){
        final TranslateState translateState = state.getTranslateState();

        if (text.isEmpty()){
            translateState.setText(text);
            translateState.setWord(null);
            return;
        }

        translateState.setText(text);

        translate();
    }

    void onSelectHistoryPage(){
        HistoryState historyState = state.getHistoryState();
        historyState.setSelectedPage(HistoryState.HistoryPage);
        if (view != null) {
            view.selectHistoryPage();
        }
    }

    void onSelectFavoritePage(){
        HistoryState historyState = state.getHistoryState();
        historyState.setSelectedPage(HistoryState.FavoritePage);
        if (view != null) {
            view.selectFavoritePage();
        }
    }

    void onReadyHistoryFragment() {

        HistoryState historyState = state.getHistoryState();
        String text = historyState.getSearchText();
        if (view == null)
            return;

        view.showProgress();
        YatInteractor.YatLoadCallback callback = new YatInteractor.YatLoadCallback() {
            @Override
            public void success(List<Word> words) {
                if (view == null)
                    return;
                view.hideProgress();
                view.showWords(words);
            }

            @Override
            public void failure(Exception e) {
                if (view == null)
                    return;
                view.hideProgress();
                view.showLoadErrorMessage();
            }
        };

        if (text.isEmpty()) {
            if (historyState.getSelectedPage() == HistoryState.HistoryPage) {
                inter.loadHistory(callback);
            } else {
                inter.loadFavorites(callback);
            }
        } else {
            if (historyState.getSelectedPage() == HistoryState.HistoryPage){
                inter.searchHistory(text, callback);
            } else {
                inter.searchFavorites(text, callback);
            }
        }

    }

    void onDelete() {
        final HistoryState historyState = state.getHistoryState();

        if (view == null)
            return;

        view.showProgress();
        YatInteractor.YatDeleteCallback callback = new YatInteractor.YatDeleteCallback() {
            @Override
            public void success() {

                if (view == null)
                    return;

                view.hideProgress();
                view.showWords(new ArrayList<Word>());

                if (historyState.getSelectedPage() == HistoryState.FavoritePage){
                    TranslateState translateState = state.getTranslateState();
                    final Word word = translateState.getWord();
                    if (word != null){
                        word.setFavorite(false);
                    }
                }
            }

            @Override
            public void failure(Exception e) {
                if (view == null)
                    return;

                view.hideProgress();
                view.showDeleteErrorMessage();
            }
        };

        if (historyState.getSelectedPage() == HistoryState.HistoryPage){
            inter.deleteHistory(callback);
        } else {
            inter.deleteFavorites(callback);
        }
    }

    void onFavoriteChanged() {
        TranslateState translateState = state.getTranslateState();
        final Word word = translateState.getWord();

        if (view == null || word == null || word.getId() == 0)
            return;

        view.showProgress();
        inter.setFavorite((int)word.getId(), new YatInteractor.YatFavoriteChangedCallback() {
            @Override
            public void success(boolean isFavorite) {

                word.setFavorite(isFavorite);

                if (view == null)
                    return;

                view.hideProgress();
                view.changeFavorite(isFavorite);
            }

            @Override
            public void failure(Exception e) {
                if (view == null)
                    return;

                view.hideProgress();

            }
        });

    }

    void onFullscreen(int fullscreen) {
        TranslateState translateState = state.getTranslateState();
        translateState.setFullscreen(fullscreen);
    }

    void onSearch(String text){
        final HistoryState historyState = state.getHistoryState();
        historyState.setSearchText(text);

        if (view == null)
            return;

        YatInteractor.YatLoadCallback callback = new YatInteractor.YatLoadCallback() {
            @Override
            public void success(List<Word> word) {
                if (view == null)
                    return;

                view.hideProgress();
                view.showWords(word);
            }

            @Override
            public void failure(Exception e) {
                if (view == null)
                    return;

                view.hideProgress();
                view.showLoadErrorMessage();
            }
        };

        view.showProgress();
        if (historyState.getSelectedPage() == HistoryState.HistoryPage){
            inter.searchHistory(text, callback);
        } else {
            inter.searchFavorites(text, callback);
        }

    }

}
