package ru.merkulyevsasha.yat.presentation.pojo;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class HistoryState {

    public static final int HistoryPage = 0;
    public static final int FavoritePage = 1;


    private int selectedPage;

    private final String[] searchText;


    public HistoryState(int selectedPage){
        this.selectedPage = selectedPage;
        searchText = new String[2];
        searchText[HistoryPage] = "";
        searchText[FavoritePage] = "";
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    public void setSearchText(String text) {
        searchText[selectedPage] = text;
    }

    public String getSearchText(){
        return searchText[selectedPage];
    }
}
