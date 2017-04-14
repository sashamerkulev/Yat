package ru.merkulyevsasha.yat.presentation.controls;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.merkulyevsasha.yat.R;

/**
 * Created by sasha_merkulev on 14.04.2017.
 */

public class ToolbarTitles extends LinearLayout {

    private TextView title1;
    private TextView title2;

    private onTitleClickListener listener;

    public ToolbarTitles(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        title1 = new TextView(context, attrs);
        setTeaxtAppearance(context, title1);
        LayoutParams params = new LayoutParams(context, attrs);
        params.setMarginEnd(50);
        title1.setLayoutParams(params);

        title2 = new TextView(context, attrs);
        setTeaxtAppearance(context, title2);

        title1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTitleLeftClick();
            }
        });

        title2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTitleRightClick();
            }
        });

        setOrientation(HORIZONTAL);
        setGravity(TEXT_ALIGNMENT_CENTER);
        addView(title1);
        addView(title2);
    }

    private void setTeaxtAppearance(Context context, TextView textview){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textview.setTextAppearance(android.R.style.TextAppearance_Large);
        } else {
            textview.setTextAppearance(context, android.R.style.TextAppearance_Large);
        }
    }

    public void setOnTitleClickListener(onTitleClickListener listener){
        this.listener = listener;
    }

    public void setLeftTitle(String text){
        title1.setText(text);
        title1.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        title1.setVisibility(View.VISIBLE);
        title2.setVisibility(View.GONE);
    }

    public void setLeftAndRightTitle(String left, String right){
        title1.setText(left);
        title1.setVisibility(View.VISIBLE);
        title1.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        title2.setText(right);
        title2.setVisibility(View.VISIBLE);
        title2.setTextColor(ContextCompat.getColor(getContext(), R.color.brown));
    }

    public void selectLeftTitle(){
        title1.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        title2.setTextColor(ContextCompat.getColor(getContext(), R.color.brown));
    }

    public void selectRightTitle(){
        title1.setTextColor(ContextCompat.getColor(getContext(), R.color.brown));
        title2.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
    }

    public interface onTitleClickListener{
        void onTitleLeftClick();
        void onTitleRightClick();
    }
}
