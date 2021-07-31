package com.boostcourse.iron.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment<VM extends BaseViewModel, VDB extends ViewDataBinding> extends Fragment {

    protected VM viewModel;
    protected VDB binding;

    @LayoutRes
    abstract protected int getLayoutRes();

    protected Class<VM> getViewModelClazz() {
        return null;
    }

    protected VM getViewModel() {
        return new ViewModelProvider(requireActivity()).get(getViewModelClazz());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        binding.setLifecycleOwner(this);

        init();

        return binding.getRoot();
    }

    public void init() {
        if (getViewModelClazz() != null && getViewModel() != null) {
            viewModel = getViewModel();
        }
    }
}
