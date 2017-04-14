package ru.merkulyevsasha.yat.presentation.history;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Word;

/**
 * Created by sasha_merkulev on 13.04.2017.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private static final String TAG = HistoryAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Word> mList;
    private final OnItemClickListener listener;

    public HistoryAdapter(Context context, List<Word> list, OnItemClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Word item = mList.get(position);

        if (item.isFavorite()){
            holder.favorite.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            holder.favorite.clearColorFilter();
        }

        holder.text.setText(item.getText());
        holder.translatedText.setText(item.getTranslatedText());
        holder.lang.setText(item.getLanguage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setItems(List<Word> words) {
        mList = words;
    }

    public interface OnItemClickListener {
        void onItemClick(Word item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView favorite;

        final TextView text;
        final TextView translatedText;
        final TextView lang;

        public ViewHolder(View itemView) {
            super(itemView);

            favorite = (ImageView)itemView.findViewById(R.id.imageview_favorite);

            text = (TextView)itemView.findViewById(R.id.textview_text);
            translatedText = (TextView)itemView.findViewById(R.id.textview_translated_text);
            lang = (TextView)itemView.findViewById(R.id.textview_lang);

        }
    }
}