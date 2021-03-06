package com.boostcourse.iron.ui.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.ui.fragment.MovieScreenFragment;

import java.util.ArrayList;

public class MovieFragmentStateAdapter extends FragmentStateAdapter {

    private static final int FRAGMENT_MAX_ITEM_COUNT = 10000;
    private final int ITEM_DIVIDER_COUNT; //무한 스크롤링처럼 보여지게 하기 위해 적용해 보았습니다.

    private final ArrayList<MovieInfo> movieList = new ArrayList<>();

    public MovieFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<MovieInfo> movieList) {
        super(fragmentActivity);

        ITEM_DIVIDER_COUNT = movieList.size();
        this.movieList.addAll(movieList);
    }

    /**
     * ViewPager2에서 보여지는 각 인덱스의 영화내용에 맞게 MovieScreenFragment로 전달합니다.
     *
     * @param position 현재 화면에 보여질 Fragment의 position
     * @return MovieScreenFragment (화면 가운데에 위치한 Fragment)
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int modPosition = getModuloPosition(position);
        MovieScreenFragment movieScreenFragment = new MovieScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movieInfo", this.movieList.get(modPosition));
        movieScreenFragment.setArguments(bundle);
        return movieScreenFragment;
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_MAX_ITEM_COUNT;
    }

    private int getModuloPosition(int position) {
        return position % ITEM_DIVIDER_COUNT;
    }
}
