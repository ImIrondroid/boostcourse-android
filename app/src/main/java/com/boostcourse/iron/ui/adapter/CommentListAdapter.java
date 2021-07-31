package com.boostcourse.iron.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.boostcourse.iron.R;
import com.boostcourse.iron.databinding.ItemCommentBinding;
import com.boostcourse.iron.ui.base.BaseViewHolder;
import com.boostcourse.iron.ui.model.MovieComment;

public class CommentListAdapter extends ListAdapter<MovieComment, CommentListAdapter.CommentListViewHolder> {

    public static final int PREVIEW_ITEM_MAX_SIZE = 3;

    private final LayoutInflater inflater;
    private CommentListCallback callback;

    public CommentListAdapter(Context context) {
        super(new CommentDiffUtil());
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface CommentListCallback {
        void onClickedItemRecommend(int position);
    }

    public void setRecommendCallbackListener(CommentListCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public CommentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentListViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListViewHolder holder, int position) {
        MovieComment comment = getCurrentList().get(position);
        holder.binding.setItem(comment);

        holder.setCommentClickListener(callback);
    }

    static class CommentListViewHolder extends BaseViewHolder<ItemCommentBinding> {

        private CommentListCallback callback;

        public CommentListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void init() {
            binding.llUserRecommendGroup.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (callback != null) {
                    callback.onClickedItemRecommend(position);
                }
            });
        }

        public void setCommentClickListener(CommentListCallback callback) {
            this.callback = callback;
        }
    }
}