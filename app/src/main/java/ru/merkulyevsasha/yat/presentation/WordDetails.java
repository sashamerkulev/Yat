package ru.merkulyevsasha.yat.presentation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.helper.GsonHelper;
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

        word = GsonHelper.json2Word(word.getJson());

        RecyclerView translates = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        translates.setLayoutManager(layoutManager);
        TranslateDefAdapter adapter = new TranslateDefAdapter(this, word.getDef());
        translates.setAdapter(adapter);

        View yandexLink = findViewById(R.id.textview_yandex_label);
        yandexLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://tech.yandex.ru/dictionary/"));
                startActivity(intent);
            }
        });

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
