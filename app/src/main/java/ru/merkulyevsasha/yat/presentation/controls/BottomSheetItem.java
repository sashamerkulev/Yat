package ru.merkulyevsasha.yat.presentation.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ru.merkulyevsasha.yat.R;

/**
 * Created by sasha_merkulev on 13.04.2017.
 */

public class BottomSheetItem extends RelativeLayout {

    private final ImageView imageView;
    private final View view;

    public BottomSheetItem(Context context, @Nullable AttributeSet attrs ) {
        super(context, attrs);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.BottomSheetItem, 0, 0);
        try {

            Drawable icon = attributes.getDrawable(R.styleable.BottomSheetItem_iconsrc);

            imageView = new ImageView(context);
            imageView.setImageDrawable(icon);
            imageView.setClickable(false);
            addView(imageView);

            view = new View(context);
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            addView(view);

        } finally {
            attributes.recycle();
        }

    }

    public void select(){
        imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
        view.setVisibility(View.VISIBLE);
    }

    public void unselect(){
        imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray));
        view.setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int w = getWidth();
            int h = getHeight();

            if (child instanceof ImageView) {

                int w1 = child.getWidth();
                int h1 = child.getHeight();

                child.layout(w/2 - w1/2, h/2 - h1/2, w/2 - w1/2 + w1, h/2 - h1/2 + h1);

            } else {
                child.layout(0, h-10, w, h);
            }
        }

    }


}
