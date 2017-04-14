package ru.merkulyevsasha.yat.presentation.translate;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Def;

/**
 * Created by sasha_merkulev on 12.04.2017.
 */
public class TranslateDefAdapter extends RecyclerView.Adapter<TranslateDefAdapter.ViewHolder> {

    private static final String TAG = TranslateDefAdapter.class.getSimpleName();
    private Context mContext;
    private List<Def> mList;

    public TranslateDefAdapter(Context context, List<Def> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_def_translate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Def item = mList.get(position);

        holder.pos.setText(item.getPos());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        holder.tr.setLayoutManager(layoutManager);
        TranslateTrAdapter adapter = new TranslateTrAdapter(mContext, item.getTr());
        holder.tr.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    void setItems(List<Def> items){
        mList = items;
    }

    public interface OnItemClickListener {
        void onItemClick(Def item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView pos;
        final RecyclerView tr;

        ViewHolder(View itemView) {
            super(itemView);

            pos = (TextView)itemView.findViewById(R.id.textview_pos);
            tr = (RecyclerView)itemView.findViewById(R.id.recycler_tr);
        }
    }
}