package ru.merkulyevsasha.yat.presentation.translate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.merkulyevsasha.yat.R;
import ru.merkulyevsasha.yat.pojo.Mean;
import ru.merkulyevsasha.yat.pojo.Syn;
import ru.merkulyevsasha.yat.pojo.Tr;

/**
 * Created by sasha_merkulev on 10.04.2017.
 */

public class TranslateTrAdapter extends RecyclerView.Adapter<TranslateTrAdapter.ViewHolder> {

    private static final String TAG = TranslateTrAdapter.class.getSimpleName();
    private Context mContext;
    private List<Tr> mList;

    TranslateTrAdapter(Context context, List<Tr> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_tr_translate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tr item = mList.get(position);

        int number = holder.getAdapterPosition()+1;
        holder.number.setText(String.valueOf(number));
        StringBuilder sbsyn = new StringBuilder();
        sbsyn.append(item.getText());
        if (item.getSyn() != null) {
            for (Syn syn : item.getSyn()) {
                sbsyn.append(", ");
                sbsyn.append(syn.getText());
            }
        }
        holder.text.setText(sbsyn.toString());

        StringBuilder sbmean = new StringBuilder();
        if (item.getMean() != null) {
            for (Mean mean : item.getMean()) {
                if (sbmean.length() > 0){
                    sbmean.append(", ");
                }
                sbmean.append(mean.getText());
            }
        }
        String mean = sbmean.toString();
        if (mean.length() == 0) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText("("+sbmean.toString()+")");
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setItems(List<Tr> items){
        mList = items;
    }

    public interface OnItemClickListener {
        void onItemClick(Tr item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView number;
        final TextView text;
        final TextView description;


        ViewHolder(View itemView) {
            super(itemView);

            number = (TextView)itemView.findViewById(R.id.textview_rownumber);
            text = (TextView)itemView.findViewById(R.id.textview_translated_text);
            description = (TextView)itemView.findViewById(R.id.textview_description);

        }
    }
}