package ru.merkulyevsasha.yat.presentation.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    HistoryAdapter adapter;
    ViewPager pager;

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
        List<View> pages = new ArrayList<>();
        pages.add(history);
        pages.add(favorite);

        pager=(ViewPager)rootView.findViewById(R.id.pager);
        pager.setAdapter(new HistoryPagerAdapter(pages));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset == 0.0F) {
                    RecyclerView translates = position == 0
                            ? (RecyclerView) history.findViewById(R.id.recyclerview)
                            : (RecyclerView) favorite.findViewById(R.id.recyclerview);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    translates.setLayoutManager(layoutManager);

                    adapter = new HistoryAdapter(getActivity(), new ArrayList<Word>());
                    translates.setAdapter(adapter);

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
        adapter.setItems(words);
        adapter.notifyDataSetChanged();
    }

    public interface onPageChangeListener {
        void onPageSelected(int position);
    }

    public interface onHistoryFragmentReadyListener {
        void onReadyHistoryFragment();
    }

}
