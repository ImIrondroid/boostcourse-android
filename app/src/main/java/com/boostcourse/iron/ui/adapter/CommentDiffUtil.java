package com.boostcourse.iron.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.boostcourse.iron.ui.model.MovieComment;

public class CommentDiffUtil extends DiffUtil.ItemCallback<MovieComment> {

    @Override
    public boolean areItemsTheSame(@NonNull MovieComment oldItem, @NonNull MovieComment newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull MovieComment oldItem, @NonNull MovieComment newItem) {
        return oldItem.getContents().equals(newItem.getContents()) &&
                oldItem.getMovieId() == newItem.getMovieId() &&
                oldItem.getRating() == newItem.getRating() &&
                oldItem.getRecommend() == newItem.getRecommend() &&
                oldItem.getTime().equals(newItem.getTime());
    }
}