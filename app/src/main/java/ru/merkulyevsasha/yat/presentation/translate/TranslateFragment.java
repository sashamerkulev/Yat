package ru.merkulyevsasha.yat.presentation.translate;

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

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Def;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 03.04.2017.
 */

public class TranslateFragment extends Fragment {

    private static final String KEY_TEXT = "TEXT";
    private static final String KEY_WORD = "WORD";
    private static final String KEY_FULLSCREEN = "FULLSCREEN";

    private View layoutText;

    private EditText text;
    private ImageButton buttonFavorite;
    private ImageButton buttonFullscreen;

    private TextView translatedText;
    private TextView sourceText;

    private TranslateDefAdapter adapter;

    public static TranslateFragment getInstance(String text, Word word, int fullscreen){

        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        args.putSerializable(KEY_WORD, word);
        args.putInt(KEY_FULLSCREEN, fullscreen);
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
                if (getActivity() instanceof OnTextCompleteListener){
                    ((OnTextCompleteListener)getActivity()).onTextComplete(text.getText().toString());
                }
                return false;
            }
        });

        ImageButton buttonClear = (ImageButton) view.findViewById(R.id.clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                setTranslates(null);
                if (getActivity() instanceof OnTextCompleteListener){
                    ((OnTextCompleteListener)getActivity()).onTextComplete(text.getText().toString());
                }
            }
        });

        ImageButton buttonMicrophone = (ImageButton) view.findViewById(R.id.microphone);
        buttonMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof OnTextSpeechRecognitionListener){
                    ((OnTextSpeechRecognitionListener)getActivity()).onTextRecognition();
                }
            }
        });

        ImageButton buttonSpeakSource = (ImageButton) view.findViewById(R.id.speek_source);
        buttonSpeakSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof OnTextToSpeechListener){
                    String tts = text.getText().toString();
                    if (!tts.isEmpty()) {
                        ((OnTextToSpeechListener) getActivity()).onTextToSpeech(tts);
                    }
                }
            }
        });

        ImageButton buttonSpeakTranslated = (ImageButton) view.findViewById(R.id.speek_dest);
        buttonSpeakTranslated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof OnTextToSpeechListener){
                    String tts = translatedText.getText().toString();
                    if (!tts.isEmpty()) {
                        ((OnTextToSpeechListener) getActivity()).onTranslatedTextToSpeech(translatedText.getText().toString());
                    }
                }
            }
        });

        buttonFavorite = (ImageButton)view.findViewById(R.id.favorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof OnFavoriteListener){
                    ((OnFavoriteListener)getActivity()).onFavoriteChanged();
                }
            }
        });

        layoutText = view.findViewById(R.id.layout_text);

        buttonFullscreen = (ImageButton)view.findViewById(R.id.button_fullscreen);
        buttonFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutText.getVisibility() == View.GONE){
                    layoutText.setVisibility(View.VISIBLE);
                    buttonFullscreen.clearColorFilter();
                    if (getActivity() instanceof OnFullscrrenButtonListener){
                        ((OnFullscrrenButtonListener)getActivity()).onFullscreenButtonClick(0);
                    }
                } else {
                    layoutText.setVisibility(View.GONE);
                    buttonFullscreen.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    ((OnFullscrrenButtonListener)getActivity()).onFullscreenButtonClick(1);
                }
            }
        });

        Bundle args = getArguments();
        String argsText = args.getString(KEY_TEXT);
        Word word = (Word)args.getSerializable(KEY_WORD);
        text.setText(argsText);

        int fullscreen = args.getInt(KEY_FULLSCREEN);
        setFullscreenIconColor(fullscreen == 1);

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
        if (word == null || word.getDef() == null || word.getDef().size() == 0) {

            sourceText.setText("");
            translatedText.setText("");

            buttonFavorite.clearColorFilter();

            adapter.setItems(new ArrayList<Def>());
            adapter.notifyDataSetChanged();

            return;
        }

        translatedText.setText(word.getTranslatedText());
        sourceText.setText(word.getText());

        setFavoriteIconColor(word.isFavorite());

        adapter.setItems(word.getDef());
        adapter.notifyDataSetChanged();
    }

    private void setFavoriteIconColor(boolean isFavorite){
        if (isFavorite) {
            buttonFavorite.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        } else {
            buttonFavorite.clearColorFilter();
        }
    }

    private void setFullscreenIconColor(boolean isFullscreen){
        if (isFullscreen) {
            buttonFullscreen.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            layoutText.setVisibility(View.GONE);
        } else {
            buttonFullscreen.clearColorFilter();
            layoutText.setVisibility(View.VISIBLE);
        }
    }

    public void setFavorite(boolean isFavorite) {
        setFavoriteIconColor(isFavorite);
    }

    public void setRecognitionText(String text) {
        this.text.setText(text);
        if (getActivity() instanceof OnTextCompleteListener){
            ((OnTextCompleteListener)getActivity()).onTextComplete(text);
        }
    }

    public interface OnTextCompleteListener {
        void onTextComplete(String text);
    }

    public interface OnFavoriteListener{
        void onFavoriteChanged();
    }

    public interface OnFullscrrenButtonListener {
        void onFullscreenButtonClick(int fullscreen);
    }

    public interface OnTextToSpeechListener {
        void onTextToSpeech(String text);
        void onTranslatedTextToSpeech(String text);
    }

    public interface OnTextSpeechRecognitionListener{
        void onTextRecognition();
    }

}
