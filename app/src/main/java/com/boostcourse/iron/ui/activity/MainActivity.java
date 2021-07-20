package com.boostcourse.iron.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.data.MovieRepository;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.ui.AppFragmentFactory;
import com.boostcourse.iron.ui.FragmentCallback;
import com.boostcourse.iron.ui.fragment.LoadingFragment;
import com.boostcourse.iron.ui.fragment.MovieDetailFragment;
import com.boostcourse.iron.ui.fragment.MovieListFragment;
import com.boostcourse.iron.ui.MovieViewModel;
import com.boostcourse.iron.ui.base.BaseActivity;
import com.boostcourse.iron.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MovieViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    private FragmentManager manager;
    private DrawerLayout drawer;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected int getTitleRes() {
        return R.string.app_bar_movie_list;
    }

    @Override
    protected MovieViewModel getViewModel() {
        return new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MovieViewModel(new MovieRepository(MainActivity.this));
            }
        }).get(MovieViewModel.class);
    }

    @Override
    protected void init() {
        super.init();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        manager = getSupportFragmentManager();
        manager.setFragmentFactory(new AppFragmentFactory());

        loadMovieList();
    }

    /**
     * @param item NavigationView에서 보여지는 각 Menu
     * @return false (클릭시 메뉴 선택 해제)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_list) { //MovieDetailFragment이 보이는 상태일 때만 MovieListFragment로 돌아오기가 가능합니다.
            if (manager.getBackStackEntryCount() > 0)
                onBackPressed();
        } else if (itemId == R.id.nav_api) {
            ToastUtil.show(this, R.string.menu_api);
        } else if (itemId == R.id.nav_book) {
            ToastUtil.show(this, R.string.menu_book);
        } else if (itemId == R.id.nav_settings) {
            ToastUtil.show(this, R.string.menu_settings_user);
        }
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    /**
     * MovieScreenFragment의 상세보기 버튼이 눌렸을 때 실행되는 콜백 메서드
     *
     * @param movieId 영화 상세 정보를 조회하기 위한 Key를 전달하기 위한 파라미터
     */
    @Override
    public void onClickedOnFragment(int movieId) {
        showMovieDetailFragment(movieId);
    }

    /**
     * Fragment에서 요청하는 데이터 처리
     *
     * @param type     URL 호출 타입
     * @param bundle   저장 데이터
     * @param listener 데이터 처리 후 이벤트 리스너
     */
    @Override
    public void sendRequestOnFragment(Directory type, Bundle bundle, FinishListener listener) {
        viewModel.sendRequest(type, bundle, listener);
    }

    /**
     * 영화 리스트 예매율 순으로 영화 리스트 조회 (type : 1)
     */
    private void loadMovieList() {
        showLoading();

        Bundle bundle = new Bundle();
        bundle.putString("type", "1");

        viewModel.sendRequest(Directory.MOVIE, bundle, new FinishListener() {
            @Override
            public void onFinish(List<? extends MovieResponse> result) {
                showMovieListFragment((List<MovieInfo>) result);
            }

            @Override
            public void onError(Exception e) {
                Log.e("loadMovieInfoList()", e.getMessage());
            }

            @Override
            public void onError() {
                Log.e("loadMovieInfoList()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * MovieListFragment의 ViewPager2에 필요한 영화 리스트 전달
     *
     * @param movieList 영화 리스트
     */
    private void showMovieListFragment(List<MovieInfo> movieList) {
        MovieListFragment listFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("movieList", new ArrayList<>(movieList));
        listFragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.container, listFragment).commit();
    }

    /**
     * MovieDetailFragment의 UI 작업에 필요한 영화 상세 내용 전달
     *
     * @param movieId 영화 아이디
     */
    private void showMovieDetailFragment(int movieId) {
        Fragment movieDetailFragment = manager.getFragmentFactory().instantiate(getClassLoader(), MovieDetailFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putInt("movieId", movieId);
        movieDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, movieDetailFragment);
        transaction.addToBackStack(null); //MovieDetailFragment에서 뒤로가기 버튼을 눌렀을 때 MovieListFragment로 되돌아오기 위해 설정해줍니다.
        transaction.commit();
    }

    /**
     * 네트워크 통신이 시작되면 ProgressBar 로딩이 시작되고 데이터가 조회되면 종료
     */
    private void showLoading() {
        Fragment loadingDialogFragment =
                (LoadingFragment) manager.getFragmentFactory().instantiate(getClassLoader(), LoadingFragment.class.getName());
        manager.beginTransaction().replace(R.id.container, loadingDialogFragment).commit();
    }
}