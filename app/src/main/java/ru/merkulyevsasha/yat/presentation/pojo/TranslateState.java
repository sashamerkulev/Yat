package ru.merkulyevsasha.yat.presentation.pojo;

import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class TranslateState {

    private int selectedLanguage;
    private int fullscreen;
    private String text;
    private Word word;

    public TranslateState(int selectedLanguage, String text, int fullscreen){
        this.selectedLanguage = selectedLanguage;
        this.text = text;
        this.fullscreen = fullscreen;
    }

    public int getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(int selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWord(Word word){ this.word = word; }

    public Word getWord() {
        return word;
    }

    public int getFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(int fullscreen) {
        this.fullscreen = fullscreen;
    }
}
