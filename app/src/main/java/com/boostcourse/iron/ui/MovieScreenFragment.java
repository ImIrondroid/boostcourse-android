package com.boostcourse.iron.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.boostcourse.iron.model.MovieInfo;
import com.boostcourse.iron.R;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieScreenFragment extends Fragment {

    private FragmentCallback callback;

    private ImageView ivMovieImage;
    private TextView tvMovieId;
    private TextView tvMovieTitle;
    private TextView tvMovieReservationRate;
    private TextView tvMovieGrade;
    private TextView tvMovieReleaseDateDiff;
    private Button btnMovieDetail;

    private MovieInfo movieInfo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_screen, container, false);

        viewInit(rootView);
        viewEvent();

        return rootView;
    }

    private void viewInit(ViewGroup rootView) {
        ivMovieImage = (ImageView) rootView.findViewById(R.id.iv_movie_image);
        tvMovieId = (TextView) rootView.findViewById(R.id.tv_movie_id);
        tvMovieTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        tvMovieReservationRate = (TextView) rootView.findViewById(R.id.tv_movie_reservation_rate);
        tvMovieGrade = (TextView) rootView.findViewById(R.id.tv_movie_grade);
        tvMovieReleaseDateDiff = (TextView) rootView.findViewById(R.id.tv_movie_release_date_diff);
        btnMovieDetail = (Button) rootView.findViewById(R.id.btn_movie_detail);

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
    }

    private void viewEvent() {
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