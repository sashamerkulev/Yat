package ru.merkulyevsasha.yat.presentation.history;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 14.04.2017.
 */

class HistoryFragmentPage {

    private final HistoryAdapter adapter;

    private final EditText edittextSearch;

    HistoryFragmentPage(Context context, View view, String hint,
                               final HistoryAdapter.OnItemClickListener itemClickListener,
                               final onSearchListener onSearchListener,
                               final HistoryAdapter.OnFavoriteIconListener onFavoriteIconListener){

        edittextSearch = (EditText)view.findViewById(R.id.edittext_searchtext);
        edittextSearch.setHint(hint);
        edittextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSearchListener.onSearch(edittextSearch.getText().toString());
                return false;
            }
        });

        ImageView buttonClear = (ImageView) view.findViewById(R.id.imageview_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittextSearch.setText("");
                onSearchListener.onSearch(edittextSearch.getText().toString());
            }
        });
        ImageView butonSearch = (ImageView) view.findViewById(R.id.imageview_search);
        butonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchListener.onSearch(edittextSearch.getText().toString());
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManagerHistory = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManagerHistory);

        adapter = new HistoryAdapter(context, new ArrayList<Word>(), itemClickListener, onFavoriteIconListener);
        recyclerView.setAdapter(adapter);
    }

    void showWords(List<Word> words) {
        adapter.setItems(words);
        adapter.notifyDataSetChanged();
    }

    void setSearchText(String text){
        edittextSearch.setText(text);
    }

    void removeItem(int position){
        adapter.remove(position);
    }

    public void changeItem(int position, boolean isFavorite) {
        adapter.changeItem(position, isFavorite);
    }

    interface onSearchListener{
        void onSearch(String text);
    }


}
