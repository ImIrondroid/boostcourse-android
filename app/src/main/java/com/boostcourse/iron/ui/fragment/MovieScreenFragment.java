package com.boostcourse.iron.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.FragmentCallback;
import com.boostcourse.iron.ui.base.BaseFragment;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieScreenFragment extends BaseFragment {

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
    public void init(ViewGroup rootView) {
        ImageView ivMovieImage = (ImageView) rootView.findViewById(R.id.iv_movie_image);
        TextView tvMovieId = (TextView) rootView.findViewById(R.id.tv_movie_id);
        TextView tvMovieTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        TextView tvMovieReservationRate = (TextView) rootView.findViewById(R.id.tv_movie_reservation_rate);
        TextView tvMovieGrade = (TextView) rootView.findViewById(R.id.tv_movie_audience_rating);
        TextView tvMovieReleaseDateDiff = (TextView) rootView.findViewById(R.id.tv_movie_release_date_diff);
        Button btnMovieDetail = (Button) rootView.findViewById(R.id.btn_movie_detail);

        Bundle bundle = getArguments();
        if (bundle != null) {
            movieInfo = bundle.getParcelable("movieInfo");

            Glide.with(requireActivity()).load(movieInfo.getImage()).placeholder(R.drawable.image_not_available).into(ivMovieImage);
            tvMovieId.setText(String.valueOf(movieInfo.getId()));
            tvMovieTitle.setText(movieInfo.getTitle());
            tvMovieReservationRate.setText(String.valueOf(movieInfo.getReservation_rate()));
            tvMovieGrade.setText(String.valueOf(movieInfo.getGrade()));
            tvMovieReleaseDateDiff.setText(getDiffDate(movieInfo.getDate()));
        }

        btnMovieDetail.setOnClickListener(view -> {
            if (callback != null) callback.onClickedOnFragment(movieInfo.getId());
        });
    }

    /**
     * 오늘 날짜와 영화 개봉 예정일의 차이를 이용하여 화면에 표시된 날짜를 구합니다.
     *
     * @param date 영화 개봉 예정일
     * @return 화면에 표시될 잔여 날짜 예) D-10 or 오늘 개봉 or D+20
     */
    @SuppressLint("SimpleDateFormat")
    private String getDiffDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dDay = "";
        Date todayDate = new Date();
        long todayTime = todayDate.getTime();
        try {
            Date releaseDate = format.parse(date);
            long releaseTime = releaseDate.getTime();
            long diffTime = todayTime - releaseTime;
            long divider = 24 * 60 * 60 * 1000;
            long realTime = diffTime / divider;
            if (realTime > 0) { //오늘 이전에 개봉한 영화인 경우
                dDay = "D-" + (diffTime / divider);
            } else if (realTime < 0) { //오늘 이후로 개봉할 영화인 경우
                dDay = "D+" + Math.abs(diffTime / divider);
            } else { //오늘 개봉한 영화인 경우
                dDay = "오늘 개봉";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dDay;
    }
}