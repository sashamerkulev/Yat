package ru.merkulyevsasha.yat.presentation;

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
        state.setTranslateState(new TranslateState(0, ""));
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
        view.showTranslateFragment(translateState.getSelectedLanguage(), translateState.getText(), translateState.getWord());
    }

    void onHistoryFragmentSelected(){
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.History);
        HistoryState historyState = state.getHistoryState();
        final int page = historyState.getSelectedPage();
        view.showHistoryFragment(page);

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

    void selectLanguage(int selectedLanguage){
        TranslateState translateState = state.getTranslateState();
        translateState.setSelectedLanguage(selectedLanguage);

        if (view == null)
            return;

        view.selectLanguage(selectedLanguage);
    }

    void onTextTranslate(String text){
        final TranslateState translateState = state.getTranslateState();

        if (text.isEmpty()){
            translateState.setText(text);
            translateState.setWord(null);
            return;
        }

        translateState.setText(text);

        if (text.length() < 3)
            return;

        if (view == null)
            return;

        view.showProgress();

        String language = (String) LANGUAGES.keySet().toArray()[translateState.getSelectedLanguage()];

        inter.translate(text, language, "ru", new YatInteractor.YatTranslateCallback() {
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
                view.showLoadError();
            }
        };

        if (historyState.getSelectedPage() == HistoryState.HistoryPage){
            inter.loadHistory(callback);
        } else {
            inter.loadFavorites(callback);
        }

    }
}
