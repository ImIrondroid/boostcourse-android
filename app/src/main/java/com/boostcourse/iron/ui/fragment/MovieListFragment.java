package com.boostcourse.iron.ui.fragment;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.viewpager2.widget.ViewPager2;

import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.ui.adapter.MoviePagerAdapter;
import com.boostcourse.iron.ui.base.BaseFragment;

import java.util.ArrayList;

public class MovieListFragment extends BaseFragment {

    ViewPager2 pager;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_movie_list;
    }

    @Override
    public void init(ViewGroup rootView) {
        pager = rootView.findViewById(R.id.viewPager2);
        pager.setOffscreenPageLimit(2);
        pager.setPageTransformer((page, position) -> {
            int pageVisibleMargin = getResources().getDimensionPixelOffset(R.dimen.page_size_visible);
            page.setTranslationX(position * -(2 * pageVisibleMargin)); //현재 페이지 기준으로 다음 페이지를 현재 페이지에 얼마나 화면에 보이게 할 것인지 생각하면 편한 것 같습니다.
        });

        Bundle bundle = getArguments();
        ArrayList<MovieInfo> movieList;
        if (bundle != null) { //MainActivity로 부터 전달받은 영화 리스트 입니다.
            movieList = bundle.getParcelableArrayList("movieList");
            if (!movieList.isEmpty()) {
                MoviePagerAdapter moviePagerAdapter = new MoviePagerAdapter(requireActivity(), movieList);
                pager.setAdapter(moviePagerAdapter);
            }
        }
    }
}
