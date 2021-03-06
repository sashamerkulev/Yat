package ru.merkulyevsasha.yat.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.YatApp;
import ru.merkulyevsasha.yat.pojo.Word;
import ru.merkulyevsasha.yat.presentation.controls.BottomSheetItem;
import ru.merkulyevsasha.yat.presentation.controls.ToolbarTitles;
import ru.merkulyevsasha.yat.presentation.history.HistoryFragment;
import ru.merkulyevsasha.yat.presentation.settings.SettingsFragment;
import ru.merkulyevsasha.yat.presentation.translate.TranslateFragment;


public class YatActivity extends AppCompatActivity
        implements HistoryFragment.onPageChangeListener
        , HistoryFragment.OnHistoryFragmentReadyListener
        , HistoryFragment.OnHistoryItemClickListener
        , HistoryFragment.OnSearchListener
        , HistoryFragment.OnFavoriteChangeListener
        , TranslateFragment.OnTextCompleteListener
        , TranslateFragment.OnFavoriteListener
        , TranslateFragment.OnFullscrrenButtonListener
        , TranslateFragment.OnTextToSpeechListener
        , TranslateFragment.OnTextSpeechRecognitionListener
{
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private static final String TRANSLATE_FRAGMENT = "TANSLATE";
    private static final String HISTORY_FRAGMENT = "HISTORY";
    private static final String SETTINGS_FRAGMENT = "SETTINGS";

    private MenuItem actionDelete;

    private ToolbarTitles titles;

    private BottomSheetItem itemTranslate;
    private BottomSheetItem itemHistory;
//    private BottomSheetItem itemSettings;

    private View container;
    private ProgressBar progress;

    @Inject
    YatPresenterImpl pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YatApp.getComponent().inject(this);

        if (YatPresenterImpl.LANGUAGES.size() == 0) {
            List<Lang> langs = null;
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Lang>>(){}.getType();
            try {
                InputStream ins = getResources().openRawResource(
                        getResources().getIdentifier("langs", "raw", getPackageName()));
                String result = IOUtils.toString(ins, StandardCharsets.UTF_8);
                langs = gson.fromJson(result, collectionType);
                for (Lang lang : langs) {
                    YatPresenterImpl.LANGUAGES.put(lang.lang, lang.landDisplay);
                }
            } catch (IOException e) {
                e.printStackTrace();
                finish();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        titles = (ToolbarTitles)findViewById(R.id.titles);
        titles.setOnTitleClickListener(new ToolbarTitles.onTitleClickListener() {
            @Override
            public void onTitleLeftClick() {
                pres.onTitle1Click();
            }

            @Override
            public void onTitleRightClick() {
                pres.onTitle2Click();

            }
        });

        itemTranslate = (BottomSheetItem)findViewById(R.id.layout_tranlate);
        itemHistory = (BottomSheetItem)findViewById(R.id.layout_history);
        //itemSettings = (BottomSheetItem)findViewById(R.id.layout_settings);

        itemTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pres.onTranslateFragmentSelected();
            }
        });

        itemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pres.onHistoryFragmentSelected();
            }
        });

//        itemSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pres.onSettingsFragmentSelected();
//            }
//        });

        container = findViewById(R.id.container);
        progress = (ProgressBar) findViewById(R.id.progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        actionDelete = menu.findItem(R.id.action_delete);
        if (actionDelete != null) {
            actionDelete.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_dialog_title)
                    .setIcon(R.drawable.ic_delete_black_24dp)
                    .setMessage(getString(R.string.delete_dialog_message))
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            pres.onDelete();
                        }
                    })
                    .setCancelable(false)
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pres.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        pres.onStop();
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            titles.selectLeftTitle();
            pres.onSelectHistoryPage();
        } else {
            titles.selectRightTitle();
            pres.onSelectFavoritePage();
        }
    }

    private void replaceFragmentBy(Fragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    private void setActionDeleteVisible(boolean visible){
        if (actionDelete != null) {
            actionDelete.setVisible(visible);
        }
    }

    public void showTranslateFragment(int selectedLanguage, String text, Word word, int isFullscreen) {

        itemTranslate.select();
        itemHistory.unselect();
        //itemSettings.unselect();

        TranslateFragment fragment = TranslateFragment.getInstance(text, word, isFullscreen);
        replaceFragmentBy(fragment, TRANSLATE_FRAGMENT);
        setActionDeleteVisible(false);

        titles.setLeftTitle((String) YatPresenterImpl.LANGUAGES.values().toArray()[selectedLanguage]);
    }

    public void showHistoryFragment(int selectedPage, String searchText) {

        itemTranslate.unselect();
        itemHistory.select();
        //itemSettings.unselect();

        HistoryFragment fragment = HistoryFragment.getInstance(selectedPage, searchText);
        replaceFragmentBy(fragment, HISTORY_FRAGMENT);
        setActionDeleteVisible(true);

        titles.setLeftAndRightTitle(getString(R.string.title_history), getString(R.string.title_favorite));

    }

    @Override
    public void onReadyHistoryFragment(){
        pres.onReadyHistoryFragment();
    }

    public void showSettingsFragment() {

        itemTranslate.unselect();
        itemHistory.unselect();
        //itemSettings.select();

        SettingsFragment fragment = SettingsFragment.getInstance();
        replaceFragmentBy(fragment, SETTINGS_FRAGMENT);
        setActionDeleteVisible(false);

        titles.setLeftTitle(getString(R.string.title_settings));
    }

    private String getUiLocale(){
        return Locale.getDefault().getLanguage();
    }

    public void showSelectLanguageDialog(int selectedLanguage){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);

        String[] languageItems = new String[YatPresenterImpl.LANGUAGES.size()];
        for (int i = 0; i < YatPresenterImpl.LANGUAGES.size(); i++) {
            languageItems[i] = (String)YatPresenterImpl.LANGUAGES.values().toArray()[i];
        }
        builder.setSingleChoiceItems(languageItems, selectedLanguage,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        dialog.dismiss();
                        titles.setLeftTitle((String)YatPresenterImpl.LANGUAGES.values().toArray()[item]);
                        pres.onSelectLanguage(item, getUiLocale());
                    }
                });

        builder.create().show();
    }

    public void selectHistoryPage(){
        HistoryFragment history = (HistoryFragment)getSupportFragmentManager().findFragmentByTag(HISTORY_FRAGMENT);
        if (history != null && history.isVisible()) {
            history.selectHistoryPage();
        }
    }

    public void selectFavoritePage(){
        HistoryFragment history = (HistoryFragment)getSupportFragmentManager().findFragmentByTag(HISTORY_FRAGMENT);
        if (history != null && history.isVisible()) {
            history.selectFavoritPage();
        }
    }

    @Override
    public void onTextComplete(String text) {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

         pres.onTextTranslate(text, getUiLocale());
    }

    public void showProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void showTranslateErrorMessage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(container, R.string.error_translate_message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void showTranslatedText(final Word word){
        final TranslateFragment translated = (TranslateFragment)getSupportFragmentManager().findFragmentByTag(TRANSLATE_FRAGMENT);
        if (translated != null && translated.isVisible()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    translated.setTranslates(word);
                }
            });
        }
    }

    public void showWords(final List<Word> words){
        final HistoryFragment historyFragment = (HistoryFragment)getSupportFragmentManager().findFragmentByTag(HISTORY_FRAGMENT);
        if (historyFragment != null && historyFragment.isVisible()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    historyFragment.showWords(words);
                }
            });
        }
    }

    public void showLoadErrorMessage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(container, R.string.error_load_message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void showDeleteErrorMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(container, R.string.error_delete_message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFavoriteChanged() {
        pres.onFavoriteChanged();
    }

    @Override
    public void onFavoriteChange(int position, Word word) {
        pres.onFavoriteChanged(position, word);
    }

    public void changeFavorite(final boolean isFavorite) {
        final TranslateFragment translated = (TranslateFragment)getSupportFragmentManager().findFragmentByTag(TRANSLATE_FRAGMENT);
        if (translated != null && translated.isVisible()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    translated.setFavorite(isFavorite);
                }
            });
        }
    }

    public void changeFavorite(final int position, final boolean isFavorite) {
        final HistoryFragment historyFragment = (HistoryFragment)getSupportFragmentManager().findFragmentByTag(HISTORY_FRAGMENT);
        if (historyFragment != null && historyFragment.isVisible()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    historyFragment.setFavorite(position, isFavorite);
                }
            });
        }
    }

    @Override
    public void onItemClick(Word item) {
        Intent intent = new Intent(this, WordDetails.class);
        intent.putExtra(WordDetails.KEY_WORD, item);
        startActivity(intent);
    }

    @Override
    public void onFullscreenButtonClick(int fullscreen) {
        pres.onFullscreen(fullscreen);
    }

    @Override
    public void onSearch(String text) {
        pres.onSearch(text);
    }

    @Override
    public void onTextToSpeech(String text) {
        pres.onTextToSpeech(text);
    }

    @Override
    public void onTranslatedTextToSpeech(String text) {
        pres.onTranslatedTextToSpeech(text);
    }

    public void showErrorSpeechLocaleMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(container, R.string.error_tts_message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onTextRecognition() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(container, R.string.voice_recognition_error_message, Snackbar.LENGTH_LONG).show();
                }
            });
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        // Display an hint to the user about what he should say.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.promt_text));
        // Given an hint to the recognizer about what the user is going to say
        //There are two form of language model available
        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //Start the Voice recognizer activity for the result.
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
        //If Voice recognition is successful then it returns RESULT_OK
        if(resultCode == RESULT_OK) {
            final ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!textMatchList.isEmpty()) {
                final TranslateFragment translated = (TranslateFragment)getSupportFragmentManager().findFragmentByTag(TRANSLATE_FRAGMENT);
                if (translated != null && translated.isVisible()) {
                    translated.setRecognitionText(textMatchList.get(0));
                }
            }
        } else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
            Snackbar.make(container, R.string.recognition_audio_error_message, Snackbar.LENGTH_LONG).show();
        } else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
            Snackbar.make(container, R.string.recognition_client_error_message, Snackbar.LENGTH_LONG).show();
        } else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
            Snackbar.make(container, R.string.recognition_network_error_message, Snackbar.LENGTH_LONG).show();
        } else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
            Snackbar.make(container, R.string.recognition_no_match_message, Snackbar.LENGTH_LONG).show();
        } else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
            Snackbar.make(container, R.string.recognition_server_error_message, Snackbar.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class Lang{
        @SerializedName("lang")
        @Expose
        String lang;
        @SerializedName("landDisplay")
        @Expose
        String landDisplay;
    }

}
