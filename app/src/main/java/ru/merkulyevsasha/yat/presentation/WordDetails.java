package ru.merkulyevsasha.yat.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Def;
import ru.merkulyevsasha.yat.pojo.Word;
import ru.merkulyevsasha.yat.presentation.translate.TranslateDefAdapter;

/**
 * Created by sasha_merkulev on 14.04.2017.
 */

public class WordDetails extends AppCompatActivity {

    public static final String KEY_WORD = "WORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worddetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();
        Word word = (Word) intent.getSerializableExtra(KEY_WORD);
        if (word == null) {
            finish();
        }

        setTitle(word.getText()+" ("+word.getTranslatedText()+")");

        Gson gson = new Gson();
        word = gson.fromJson(word.getJson(), Word.class);

        RecyclerView translates = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        translates.setLayoutManager(layoutManager);
        TranslateDefAdapter adapter = new TranslateDefAdapter(this, word.getDef());
        translates.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
