package com.boostcourse.iron.ui.fragment;

import com.boostcourse.iron.R;
import com.boostcourse.iron.databinding.FragmentLoadingDialogBinding;
import com.boostcourse.iron.ui.base.BaseFragment;
import com.boostcourse.iron.ui.base.BaseViewModel;

public class LoadingFragment extends BaseFragment<BaseViewModel, FragmentLoadingDialogBinding> {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_loading_dialog;
    }
}