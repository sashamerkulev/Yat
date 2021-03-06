package ru.merkulyevsasha.yat.presentation.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Word;
import ru.merkulyevsasha.yat.presentation.pojo.HistoryState;

/**
 * Created by sasha_merkulev on 03.04.2017.
 */

public class HistoryFragment extends Fragment {

    public static final String KEY_PAGE = "PAGE";
    public static final String KEY_SEARCHTEXT = "SEARCHTEXT";

    ViewPager pager;
    HistoryFragmentPage[] fragmentPages;

    public static HistoryFragment getInstance(int page, String searchText){

        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString(KEY_SEARCHTEXT, searchText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        final View favorite = inflater.inflate(R.layout.page_history, null);
        final View history = inflater.inflate(R.layout.page_history, null);

        HistoryAdapter.OnItemClickListener onItemClickListener = new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Word item) {
                if (getActivity() instanceof OnHistoryItemClickListener) {
                    ((OnHistoryItemClickListener) getActivity()).onItemClick(item);
                }
            }
        };

        HistoryFragmentPage.onSearchListener onSearchListener = new HistoryFragmentPage.onSearchListener() {
            @Override
            public void onSearch(String text) {
                onSearchHistory(text);
            }
        };

        HistoryAdapter.OnFavoriteIconListener onFavoriteIconListener = new HistoryAdapter.OnFavoriteIconListener() {
            @Override
            public void onFavoriteClick(int position, Word item) {
                if (getActivity() instanceof OnFavoriteChangeListener) {
                    ((OnFavoriteChangeListener) getActivity()).onFavoriteChange(position, item);
                }
            }
        };
        fragmentPages = new HistoryFragmentPage[2];
        fragmentPages[HistoryState.HistoryPage] = new HistoryFragmentPage(getActivity(), history, getString(R.string.search_text_history_hint),
                onItemClickListener, onSearchListener, onFavoriteIconListener);
        fragmentPages[HistoryState.FavoritePage] = new HistoryFragmentPage(getActivity(), favorite, getString(R.string.search_text_favorites_hint),
                onItemClickListener, onSearchListener, onFavoriteIconListener);

        List<View> pages = new ArrayList<>();
        pages.add(history);
        pages.add(favorite);

        pager=(ViewPager)rootView.findViewById(R.id.pager);
        pager.setAdapter(new HistoryPagerAdapter(pages));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0.0F) {
                    if (getActivity() instanceof OnHistoryFragmentReadyListener) {
                        ((OnHistoryFragmentReadyListener) getActivity()).onReadyHistoryFragment();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                ((onPageChangeListener)getActivity()).onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Bundle args = getArguments();
        String searchText = args.getString(KEY_SEARCHTEXT);
        int page = args.getInt(KEY_PAGE);
        fragmentPages[page].setSearchText(searchText);
        pager.setCurrentItem(page);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void selectHistoryPage(){
        pager.setCurrentItem(HistoryState.HistoryPage);
    }

    public void selectFavoritPage(){
        pager.setCurrentItem(HistoryState.FavoritePage);
    }

    public void showWords(List<Word> words) {
        fragmentPages[pager.getCurrentItem()].showWords(words);
    }

    private void onSearchHistory(String text){
        if (getActivity() instanceof OnSearchListener){
            ((OnSearchListener)getActivity()).onSearch(text);
        }
    }

    public void setFavorite(int position, boolean isFavorite) {
        if (pager.getCurrentItem() == HistoryState.HistoryPage) {
            fragmentPages[HistoryState.HistoryPage].changeItem(position, isFavorite);
        } else {
            fragmentPages[HistoryState.FavoritePage].removeItem(position);
        }
    }

    public interface onPageChangeListener {
        void onPageSelected(int position);
    }

    public interface OnHistoryFragmentReadyListener {
        void onReadyHistoryFragment();
    }

    public interface OnHistoryItemClickListener {
        void onItemClick(Word item);
    }

    public interface OnSearchListener {
        void onSearch(String text);
    }

    public interface OnFavoriteChangeListener {
        void onFavoriteChange(int position, Word word);
    }

}
