package com.boostcourse.iron.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.boostcourse.iron.databinding.FragmentMovieScreenBinding;
import com.boostcourse.iron.ui.base.BaseViewModel;
import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.FragmentCallback;
import com.boostcourse.iron.ui.base.BaseFragment;

public class MovieScreenFragment extends BaseFragment<BaseViewModel, FragmentMovieScreenBinding> {

    private MovieInfo movieInfo;
    private FragmentCallback callback;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_movie_screen;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (callback != null) {
            callback = null;
        }
    }

    @Override
    public void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieInfo = bundle.getParcelable("movieInfo");
            if (movieInfo != null) {
                binding.setMovieInfo(movieInfo);
            }
        }

        binding.btnMovieDetail.setOnClickListener(view -> {
            if (callback != null) callback.onClickedOnFragment(movieInfo.getId());
        });
    }
}