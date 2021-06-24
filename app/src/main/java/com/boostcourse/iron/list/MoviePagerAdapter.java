package com.boostcourse.iron.list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.boostcourse.iron.list.list.MovieFifthFragment;
import com.boostcourse.iron.list.list.MovieFirstFragment;
import com.boostcourse.iron.list.list.MovieFourthFragment;
import com.boostcourse.iron.list.list.MovieSecondFragment;
import com.boostcourse.iron.list.list.MovieSixthFragment;
import com.boostcourse.iron.list.list.MovieThirdFragment;

public class MoviePagerAdapter extends FragmentStateAdapter {

    private static final int MAX_ITEM_COUNT = 10000;
    private static final int ITEM_DIVIDER_COUNT = 6; //무한 스크롤링처럼 보여지게 하기 위해 적용해 보았습니다.

    public MoviePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int modPosition = getModuloPosition(position);
        if (modPosition == 0) {
            return new MovieFirstFragment();
        } else if (modPosition == 1) {
            return new MovieSecondFragment();
        } else if (modPosition == 2) {
            return new MovieThirdFragment();
        } else if (modPosition == 3) {
            return new MovieFourthFragment();
        } else if (modPosition == 4) {
            return new MovieFifthFragment();
        } else {
            return new MovieSixthFragment();
        }
    }

    @Override
    public int getItemCount() {
        return MAX_ITEM_COUNT;
    }

    private int getModuloPosition(int position) {
        return position % ITEM_DIVIDER_COUNT;
    }
}
