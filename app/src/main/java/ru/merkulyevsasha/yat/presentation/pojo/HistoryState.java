package ru.merkulyevsasha.yat.presentation.pojo;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class HistoryState {

    public static final int HistoryPage = 1;
    public static final int FavoritePage = 2;


    private int selectedPage;

    private String[] searchText;


    public HistoryState(int selectedPage){
        this.selectedPage = selectedPage;
        searchText = new String[2];
        searchText[0] = "";
        searchText[1] = "";
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    public void setSearchText(String text) {
        searchText[selectedPage-1] = text;
    }

    public String getSearchText(){
        return searchText[selectedPage-1];
    }
}
