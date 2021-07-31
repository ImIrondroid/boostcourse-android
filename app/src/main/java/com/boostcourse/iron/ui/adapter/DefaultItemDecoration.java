package com.boostcourse.iron.ui.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {

    private final int fullDp;
    private final int halfDp;

    public DefaultItemDecoration(Context context, int fullDp) {
        this.fullDp = dpToPx(context, fullDp);
        this.halfDp = dpToPx(context, fullDp / 2);
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

        outRect.top = fullDp;

        if (position == startIndex) {
            outRect.left = fullDp;
            outRect.right = halfDp;
        } else if (position == endIndex) {
            outRect.left = halfDp;
            outRect.right = fullDp;
        } else {
            outRect.left = halfDp;
            outRect.right = halfDp;
        }
    }
}
