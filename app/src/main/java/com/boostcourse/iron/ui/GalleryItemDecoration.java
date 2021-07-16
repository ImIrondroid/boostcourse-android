package com.boostcourse.iron.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {

    private final int size16;
    private final int size8;

    public GalleryItemDecoration(Context context) {
        size16 = dpToPx(context, 16);
        size8 = dpToPx(context, 8);
    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int startIndex = 0;
        int endIndex = parent.getItemDecorationCount() - 1;
        int position = parent.getChildAdapterPosition(view);

        outRect.top = size16;

        if(position == startIndex) {
            outRect.left = size16;
            outRect.right = size8;
        } else if(position == endIndex) {
            outRect.left = size8;
            outRect.right = size16;
        } else {
            outRect.left = size8;
            outRect.right = size8;
        }
    }
}
