package com.boostcourse.iron.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.MovieInfo;

import java.util.ArrayList;

public class MovieListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_list, container, false);

        viewInit(rootView);

        return rootView;
    }

    private void viewInit(ViewGroup rootView) {
        ViewPager2 pager = rootView.findViewById(R.id.viewPager2);
        pager.setOffscreenPageLimit(2);
        pager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                int pageVisibleMargin = getResources().getDimensionPixelOffset(R.dimen.page_size_visible);
                page.setTranslationX(position * -(2 * pageVisibleMargin)); //현재 페이지 기준으로 다음 페이지를 현재 페이지에 얼마나 화면에 보이게 할 것인지 생각하면 편한 것 같습니다.
            }
        });

        Bundle bundle = getArguments();
        ArrayList<MovieInfo> arrayList;
        if (bundle != null) { //MainActivity로 부터 전달받은 영화 리스트 입니다.
            arrayList = bundle.getParcelableArrayList("movieList");
        } else {
            arrayList = new ArrayList<>();
        }
        MoviePagerAdapter moviePagerAdapter = new MoviePagerAdapter(requireActivity(), arrayList);
        pager.setAdapter(moviePagerAdapter);
    }
}
