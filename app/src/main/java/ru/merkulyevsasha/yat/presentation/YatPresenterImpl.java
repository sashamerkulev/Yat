package ru.merkulyevsasha.yat.presentation;

import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

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

    public static LinkedHashMap<String, String> LANGUAGES = new LinkedHashMap<>();

    private YatActivity view;

    private StatePresenter state;
    private YatInteractor inter;

    private TextToSpeech ttsText = null;
    private TextToSpeech ttsTranslatedText = null;

    private Locale getLocale(String language){
        if (language.equals("fr"))
            return Locale.FRENCH;
        if (language.equals("en"))
            return Locale.UK;
        if (language.equals("de"))
            return Locale.GERMAN;
        if (language.equals("it"))
            return Locale.ITALIAN;
        return new Locale("ru");
    }

    private final TextToSpeech.OnInitListener onTextInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {

                TranslateState translateState = state.getTranslateState();
                String languages = (String) LANGUAGES.keySet().toArray()[translateState.getSelectedLanguage()];
                String language = languages.substring(0, 2);
                int result = ttsText.setLanguage(getLocale(language));
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    view.showErrorSpeechLocaleMessage();
                }
            }
        }
    };

    private final TextToSpeech.OnInitListener onTranslatedTextInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                TranslateState translateState = state.getTranslateState();
                String languages = (String) LANGUAGES.keySet().toArray()[translateState.getSelectedLanguage()];
                String language = languages.substring(3, 5);
                int result = ttsTranslatedText.setLanguage(getLocale(language));
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    view.showErrorSpeechLocaleMessage();
                }
            }
        }
    };

    public YatPresenterImpl(YatInteractor inter) {

        state = new StatePresenter();
        state.setFragments(StatePresenter.Fragments.Translate);
        state.setTranslateState(new TranslateState(0, "", 0));
        state.setHistoryState(new HistoryState(HistoryState.HistoryPage));
        state.setSettingsState(new SettingsState());
        this.inter = inter;
    }

    void onStart(YatActivity view) {
        this.view = view;
        view.hideProgress();

        StatePresenter.Fragments fragment = state.getFragments();
        if (fragment == StatePresenter.Fragments.Translate) {
            onTranslateFragmentSelected();
        } else if (fragment == StatePresenter.Fragments.History) {
            onHistoryFragmentSelected();
        } else if (fragment == StatePresenter.Fragments.Settings) {
            onSettingsFragmentSelected();
        }

    }

    void onStop() {
        this.view = null;
        ttsText = null;
        ttsTranslatedText = null;
    }

    void onTranslateFragmentSelected() {
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.Translate);
        TranslateState translateState = state.getTranslateState();

        Word word = translateState.getWord();
        if (word != null) {
            if (word.getTranslatedText()!= null && !word.getTranslatedText().isEmpty() ) {
                ttsTranslatedText = new TextToSpeech(view, onTranslatedTextInitListener);
            }
            if (word.getText()!= null && !word.getText().isEmpty() ) {
                ttsText = new TextToSpeech(view, onTextInitListener);
            }
        }

        view.showTranslateFragment(translateState.getSelectedLanguage(), translateState.getText(),
                word, translateState.getFullscreen());
    }

    void onHistoryFragmentSelected() {
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.History);
        HistoryState historyState = state.getHistoryState();
        view.showHistoryFragment(historyState.getSelectedPage(), historyState.getSearchText());

    }

    void onSettingsFragmentSelected() {
        if (view == null)
            return;

        state.setFragments(StatePresenter.Fragments.Settings);
        SettingsState settingsState = state.getSettingsState();
        view.showSettingsFragment();
    }

    void onTitle1Click() {
        if (view == null)
            return;

        StatePresenter.Fragments fragment = state.getFragments();
        if (fragment == StatePresenter.Fragments.Translate) {
            TranslateState translateState = state.getTranslateState();
            view.showSelectLanguageDialog(translateState.getSelectedLanguage());
            return;
        }

        if (fragment == StatePresenter.Fragments.History) {
            onSelectHistoryPage();
        }
    }

    void onTitle2Click() {
        if (view == null)
            return;

        StatePresenter.Fragments fragment = state.getFragments();
        if (fragment == StatePresenter.Fragments.History) {
            onSelectFavoritePage();
        }
    }

    private void translate(String localeUi) {
        final TranslateState translateState = state.getTranslateState();
        String text = translateState.getText();

        if (text.length() < 2)
            return;

        if (view == null)
            return;

        view.showProgress();
        String language = (String) LANGUAGES.keySet().toArray()[translateState.getSelectedLanguage()];
        inter.translate(translateState.getText(), language, localeUi, new YatInteractor.YatTranslateCallback() {
            @Override
            public void success(Word word) {

                translateState.setWord(word);
                if (view == null)
                    return;
                view.hideProgress();

                view.showTranslatedText(word);

                ttsTranslatedText = new TextToSpeech(view, onTranslatedTextInitListener);
                ttsText = new TextToSpeech(view, onTextInitListener);
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

    void onSelectLanguage(int selectedLanguage, String localeUi) {
        TranslateState translateState = state.getTranslateState();
        translateState.setSelectedLanguage(selectedLanguage);

        if (view == null)
            return;

        translate(localeUi);
    }

    void onTextTranslate(String text, String localeUi) {
        final TranslateState translateState = state.getTranslateState();

        if (text.isEmpty()) {
            translateState.setText(text);
            translateState.setWord(null);
            return;
        }

        translateState.setText(text);

        translate(localeUi);
    }

    void onSelectHistoryPage() {
        HistoryState historyState = state.getHistoryState();
        historyState.setSelectedPage(HistoryState.HistoryPage);
        if (view != null) {
            view.selectHistoryPage();
        }
    }

    void onSelectFavoritePage() {
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
            if (historyState.getSelectedPage() == HistoryState.HistoryPage) {
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

                TranslateState translateState = state.getTranslateState();
                final Word word = translateState.getWord();
                if (word != null && word.getId() > 0) {
                    if (historyState.getSelectedPage() == HistoryState.FavoritePage) {
                        word.setFavorite(false);
                    } else {
                        word.setId(0);
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

        if (historyState.getSelectedPage() == HistoryState.HistoryPage) {
            inter.deleteHistory(callback);
        } else {
            inter.deleteFavorites(callback);
        }
    }

    void onFavoriteChanged(final int position, final Word word) {
        TranslateState translateState = state.getTranslateState();
        final Word currentWord = translateState.getWord();

        if (view == null || word == null || word.getId() == 0)
            return;

        view.showProgress();
        inter.setFavorite((int) word.getId(), new YatInteractor.YatFavoriteChangedCallback() {
            @Override
            public void success(boolean isFavorite) {

                if (currentWord != null && currentWord.getId() == word.getId()){
                    currentWord.setFavorite(isFavorite);
                }

                if (view == null)
                    return;

                view.hideProgress();
                view.changeFavorite(position, isFavorite);
            }

            @Override
            public void failure(Exception e) {
                if (view == null)
                    return;

                view.hideProgress();

            }
        });
    }

    void onFavoriteChanged() {
        TranslateState translateState = state.getTranslateState();
        final Word word = translateState.getWord();

        if (view == null || word == null || word.getId() == 0)
            return;

        view.showProgress();
        inter.setFavorite((int) word.getId(), new YatInteractor.YatFavoriteChangedCallback() {
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

    void onTranslatedTextToSpeech(String text) {
        if (ttsTranslatedText != null)
            ttsTranslatedText.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        //mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    void onTextToSpeech(String text) {
        if (ttsText !=null)
            ttsText.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        //mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
