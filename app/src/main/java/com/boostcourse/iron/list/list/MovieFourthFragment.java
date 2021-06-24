package com.boostcourse.iron.list.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.boostcourse.iron.list.model.Movie;
import com.boostcourse.iron.R;
import com.boostcourse.iron.callback.FragmentCallback;

public class MovieFourthFragment extends Fragment {

    private FragmentCallback callback;

    private TextView tvMovieTitle;
    private Button btnMovieDetail;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)  inflater.inflate(R.layout.fragment_movie_fourth, container, false);

        viewInit(rootView);
        viewEvent();

        return rootView;
    }

    private void viewInit(ViewGroup rootView) {
        tvMovieTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        btnMovieDetail = (Button) rootView.findViewById(R.id.btn_movie_detail);
    }

    private void viewEvent() {
        btnMovieDetail.setOnClickListener(view -> {
            String name = tvMovieTitle.getText().toString();
            callback.onClickedViewsDetailButton(new Movie(name, "액션, SF, 스릴러", getString(R.string.contents4), R.drawable.image44, R.drawable.ic_15));
        });
    }
}