package com.boostcourse.iron.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public abstract class BaseFragment<VM extends BaseViewModel> extends Fragment {

    protected VM viewModel;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(getLayoutRes(), container, false);

        init(rootView);

        return rootView;
    }

    public void init(ViewGroup rootView) {
        if (getViewModelClazz() != null && getViewModel() != null) {
            viewModel = getViewModel();
        }
    }
}
