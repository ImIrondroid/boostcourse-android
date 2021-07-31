package com.boostcourse.iron.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<VDB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public VDB binding;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);

        init();
    }

    protected abstract void init();
}
