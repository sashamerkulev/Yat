package ru.merkulyevsasha.yat.presentation.pojo;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class StatePresenter {

    public enum Fragments{
        Translate,
        History,
        Settings
    }

    private Fragments fragments;
    private TranslateState translateState;
    private HistoryState historyState;
    private SettingsState settingsState;

    public Fragments getFragments() {
        return fragments;
    }

    public void setFragments(Fragments fragments) {
        this.fragments = fragments;
    }

    public TranslateState getTranslateState() {
        return translateState;
    }

    public void setTranslateState(TranslateState translateState) {
        this.translateState = translateState;
    }

    public HistoryState getHistoryState() {
        return historyState;
    }

    public void setHistoryState(HistoryState historyState) {
        this.historyState = historyState;
    }

    public SettingsState getSettingsState() {
        return settingsState;
    }

    public void setSettingsState(SettingsState settingsState) {
        this.settingsState = settingsState;
    }
}

