package ru.merkulyevsasha.yat.presentation.pojo;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class HistoryState {

    public static final int HistoryPage = 1;
    public static final int FavoritePage = 2;

    private int selectedPage;

    public HistoryState(int selectedPage){
        this.selectedPage = selectedPage;
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

}
