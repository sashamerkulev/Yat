package ru.merkulyevsasha.yat.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

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
        , HistoryFragment.onHistoryFragmentReadyListener
        , HistoryFragment.onHistoryItemClickListener
        , TranslateFragment.OnTextCompleteListener
        , TranslateFragment.OnFavoriteListener
{

    private static String TRANSLATE_FRAGMENT = "TANSLATE";
    private static String HISTORY_FRAGMENT = "HISTORY";
    private static String SETTINGS_FRAGMENT = "SETTINGS";

    private MenuItem actionDelete;

    private ToolbarTitles titles;

    private BottomSheetItem itemTranslate;
    private BottomSheetItem itemHistory;
    private BottomSheetItem itemSettings;

    private View container;
    private ProgressBar progress;

    @Inject
    YatPresenterImpl pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YatApp.getComponent().inject(this);

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
        itemSettings = (BottomSheetItem)findViewById(R.id.layout_settings);

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

        itemSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pres.onSettingsFragmentSelected();
            }
        });

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
            pres.onDelete();
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

    public void showTranslateFragment(int selectedLanguage, String text, Word word) {

        itemTranslate.select();
        itemHistory.unselect();
        itemSettings.unselect();

        TranslateFragment fragment = TranslateFragment.getInstance(text, word);
        replaceFragmentBy(fragment, TRANSLATE_FRAGMENT);
        setActionDeleteVisible(false);

        titles.setLeftTitle((String) YatPresenterImpl.LANGUAGES.values().toArray()[selectedLanguage]);
    }

    public void showHistoryFragment(int selectedPage) {

        itemTranslate.unselect();
        itemHistory.select();
        itemSettings.unselect();

        HistoryFragment fragment = HistoryFragment.getInstance(selectedPage);
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
        itemSettings.select();

        SettingsFragment fragment = SettingsFragment.getInstance();
        replaceFragmentBy(fragment, SETTINGS_FRAGMENT);
        setActionDeleteVisible(false);

        titles.setLeftTitle(getString(R.string.title_settings));
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
                        pres.onSelectLanguage(item);
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
         pres.onTextTranslate(text);

        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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

    @Override
    public void onItemClick(Word item) {
        Intent intent = new Intent(this, WordDetails.class);
        intent.putExtra(WordDetails.KEY_WORD, item);
        startActivity(intent);
    }
}
