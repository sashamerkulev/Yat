package ru.merkulyevsasha.yat.presentation.translate;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Def;
import ru.merkulyevsasha.yat.pojo.Tr;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 03.04.2017.
 */

public class TranslateFragment extends Fragment {

    private static final String KEY_TEXT = "TEXT";
    private static final String KEY_WORD = "WORD";

    private EditText text;
    private ImageButton buttonClear;
    private ImageButton buttonMicrophone;
    private ImageButton buttonSpeakSource;
    private ImageButton buttonSpeakTranslated;
    private ImageButton buttonFavorite;

    private TextView translatedText;
    private TextView sourceText;

    private TranslateDefAdapter adapter;

    public static TranslateFragment getInstance(String text, Word word){

        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        args.putSerializable(KEY_WORD, word);
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

        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        text = (EditText)view.findViewById(R.id.edittext_text);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (getActivity() instanceof OnTextComplete){
                    ((OnTextComplete)getActivity()).onTextComplete(text.getText().toString());
                }
                return false;
            }
        });

        buttonClear = (ImageButton)view.findViewById(R.id.clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                setTranslates(null);
                if (getActivity() instanceof OnTextComplete){
                    ((OnTextComplete)getActivity()).onTextComplete(text.getText().toString());
                }
            }
        });

        buttonMicrophone = (ImageButton)view.findViewById(R.id.microphone);
        buttonSpeakSource = (ImageButton)view.findViewById(R.id.speek_source);
        buttonSpeakSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonSpeakTranslated = (ImageButton)view.findViewById(R.id.speek_dest);
        buttonSpeakTranslated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonFavorite = (ImageButton)view.findViewById(R.id.favorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Bundle args = getArguments();
        String argsText = args.getString(KEY_TEXT);
        Word word = (Word)args.getSerializable(KEY_WORD);
        text.setText(argsText);

        RecyclerView translates = (RecyclerView)view.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        translates.setLayoutManager(layoutManager);
        adapter = new TranslateDefAdapter(getActivity(), new ArrayList<Def>());
        translates.setAdapter(adapter);

        translatedText = (TextView)view.findViewById(R.id.textview_translated);
        sourceText = (TextView)view.findViewById(R.id.textview_text);

        setTranslates(word);

        return view;

    }

    public void setTranslates(Word word){
        if (word == null || word.getDef().size() == 0) {

            sourceText.setText("");
            translatedText.setText("");

            buttonFavorite.clearColorFilter();

            adapter.setItems(new ArrayList<Def>());
            adapter.notifyDataSetChanged();

            return;
        }

        translatedText.setText(word.getTranslatedText());
        sourceText.setText(word.getText());

        if (word.isFavorite()) {
            buttonFavorite.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        } else {
            buttonFavorite.clearColorFilter();
        }

        adapter.setItems(word.getDef());
        adapter.notifyDataSetChanged();
    }

    public interface OnTextComplete{
        void onTextComplete(String text);
    }


}
