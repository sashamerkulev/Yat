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

public class HistoryFragmentPage {

    private HistoryAdapter adapter;

    private EditText edittextSearch;
    private ImageView butonSearch;
    private ImageView buttonClear;

    public HistoryFragmentPage(Context context, View view, String hint,
                               final HistoryAdapter.OnItemClickListener itemClickListener, final onSearchListener onSearchListener){

        edittextSearch = (EditText)view.findViewById(R.id.edittext_searchtext);
        edittextSearch.setHint(hint);
        edittextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSearchListener.onSearch(edittextSearch.getText().toString());
                return false;
            }
        });

        buttonClear = (ImageView)view.findViewById(R.id.imageview_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittextSearch.setText("");
                onSearchListener.onSearch(edittextSearch.getText().toString());
            }
        });
        butonSearch = (ImageView)view.findViewById(R.id.imageview_search);
        butonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchListener.onSearch(edittextSearch.getText().toString());
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManagerHistory = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManagerHistory);

        adapter = new HistoryAdapter(context, new ArrayList<Word>(), itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    public void showWords(List<Word> words) {
        adapter.setItems(words);
        adapter.notifyDataSetChanged();
    }

    public void setSearchText(String text){
        edittextSearch.setText(text);
    }

    public interface onSearchListener{
        void onSearch(String text);
    }

}
