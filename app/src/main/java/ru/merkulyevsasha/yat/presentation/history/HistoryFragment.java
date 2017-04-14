package ru.merkulyevsasha.yat.presentation.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    HistoryAdapter adapterHistory;
    HistoryAdapter adapterFavorites;
    ViewPager pager;

    EditText searchHistory;
    EditText searchFavorites;
    ImageView searchButtonHistory;
    ImageView searchButtonFavorites;

    public static HistoryFragment getInstance(int page){

        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
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

        final View favorite = inflater.inflate(R.layout.page_favorite, null);
        final View history = inflater.inflate(R.layout.page_history, null);

        searchHistory = (EditText)history.findViewById(R.id.edittext_searchtext);
        searchHistory.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSearchHistory(searchHistory.getText().toString());
                return false;
            }
        });

        searchFavorites = (EditText)favorite.findViewById(R.id.edittext_searchtext);
        searchFavorites.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSearchFavorites(searchFavorites.getText().toString());
                return false;
            }
        });

        searchButtonHistory = (ImageView)history.findViewById(R.id.imageview_search);
        searchButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchHistory(searchHistory.getText().toString());
            }
        });

        searchButtonFavorites = (ImageView)favorite.findViewById(R.id.imageview_search);
        searchButtonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchFavorites(searchFavorites.getText().toString());
            }
        });

        RecyclerView translatesHistory = (RecyclerView) history.findViewById(R.id.recyclerview);
        RecyclerView translatesFavorites = (RecyclerView) favorite.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManagerHistory = new LinearLayoutManager(getActivity());
        translatesHistory.setLayoutManager(layoutManagerHistory);

        LinearLayoutManager layoutManagerFavorites = new LinearLayoutManager(getActivity());
        translatesFavorites.setLayoutManager(layoutManagerFavorites);
        HistoryAdapter.OnItemClickListener listener = new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Word item) {
                if (getActivity() instanceof onHistoryItemClickListener) {
                    ((HistoryFragment.onHistoryItemClickListener) getActivity()).onItemClick(item);
                }
            }
        };

        adapterHistory = new HistoryAdapter(getActivity(), new ArrayList<Word>(), listener);
        adapterFavorites = new HistoryAdapter(getActivity(), new ArrayList<Word>(), listener);
        translatesHistory.setAdapter(adapterHistory);
        translatesFavorites.setAdapter(adapterFavorites);

        List<View> pages = new ArrayList<>();
        pages.add(history);
        pages.add(favorite);

        pager=(ViewPager)rootView.findViewById(R.id.pager);
        pager.setAdapter(new HistoryPagerAdapter(pages));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset == 0.0F) {

                    if (getActivity() instanceof onHistoryFragmentReadyListener) {
                        ((onHistoryFragmentReadyListener) getActivity()).onReadyHistoryFragment();
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
        int page = args.getInt(KEY_PAGE);
        if (page == HistoryState.HistoryPage){
            selectHistoryPage();
        } else if (page == HistoryState.FavoritePage){
            selectFavoritPage();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void selectHistoryPage(){
        pager.setCurrentItem(0);
    }

    public void selectFavoritPage(){
        pager.setCurrentItem(1);
    }

    public void showWords(List<Word> words) {
        if (pager.getCurrentItem() == 0) {
            adapterHistory.setItems(words);
            adapterHistory.notifyDataSetChanged();
        } else {
            adapterFavorites.setItems(words);
            adapterFavorites.notifyDataSetChanged();
        }
    }

    private void onSearchHistory(String text){
        if (getActivity() instanceof onSearchListener){
            ((onSearchListener)getActivity()).onSearchHistory(text);
        }
    }

    private void onSearchFavorites(String text){
        if (getActivity() instanceof onSearchListener){
            ((onSearchListener)getActivity()).onSearchFavorites(text);
        }
    }

    public interface onPageChangeListener {
        void onPageSelected(int position);
    }

    public interface onHistoryFragmentReadyListener {
        void onReadyHistoryFragment();
    }

    public interface onHistoryItemClickListener{
        void onItemClick(Word item);
    }

    public interface onSearchListener{
        void onSearchHistory(String text);
        void onSearchFavorites(String text);
    }


}
