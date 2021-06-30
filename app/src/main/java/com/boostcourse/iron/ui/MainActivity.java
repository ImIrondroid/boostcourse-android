package com.boostcourse.iron.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.callback.FragmentCallback;
import com.boostcourse.iron.data.MovieComment;
import com.boostcourse.iron.data.MovieCommentResult;
import com.boostcourse.iron.data.MovieDetail;
import com.boostcourse.iron.data.MovieDetailResult;
import com.boostcourse.iron.data.MovieInfo;
import com.boostcourse.iron.data.MovieInfoResult;
import com.boostcourse.iron.network.Directory;
import com.boostcourse.iron.network.GsonRequest;
import com.boostcourse.iron.network.VolleyHelper;
import com.boostcourse.iron.util.ToastUtil;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    private AppFragmentFactory factory;
    private FragmentManager manager;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
    }

    private void viewInit() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_bar_movie_list); //Application name과 겹쳐서 MainActivity의 이름을 동적으로 적용시켰습니다.
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        manager = getSupportFragmentManager();
        manager.setFragmentFactory(new AppFragmentFactory());
        loadMovieInfoList();
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
     * @param id 영화 상세 정보를 조회하기 위한 Key를 전달하기 위한 파라미터
     */
    @Override
    public void onClickedViewsDetailButton(int id) {
        loadMovieDetail(id);
    }

    /**
     * 영화 리스트 예매율 순으로 영화 리스트 조회 (type : 1)
     */
    private void loadMovieInfoList() {
        showLoading();

        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        VolleyHelper.getInstance(this).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.MOVIE),
                        MovieInfoResult.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                showMovieListFragment(response.getResult());
                            } else {
                                ToastUtil.show(this, R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(this, R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }

    /**
     * MovieDetailFragment에 필요한 영화 상세 내용 조회
     *
     * @param id 영화 상세 정보를 조회하기 위한 Key
     */
    private void loadMovieDetail(int id) { //영화 id값으로 영화 상세 조회
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        VolleyHelper.getInstance(this).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.DETAIL),
                        MovieDetailResult.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                loadMovieCommentList(response.getResult().get(0));
                            } else {
                                ToastUtil.show(this, R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(this, R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }

    /**
     * MovieDetailFragment의 ViewPager2에 필요한 영화 한줄평 리스트 조회
     *
     * @param movieDetail 영화 상세 내용
     */
    private void loadMovieCommentList(MovieDetail movieDetail) { //영화 id값에 대한 한줄평 조회
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(movieDetail.getId()));
        params.put("limit", String.valueOf(20));
        VolleyHelper.getInstance(this).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.COMMENT),
                        MovieCommentResult.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                showMovieDetailFragment(movieDetail, response.getResult()); //코멘트 리스트가 잘 조회되었다면 데이터와 함께 영화 상세 화면으로 이동합니다.
                            } else {
                                ToastUtil.show(this, R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(this, R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }

    /**
     * MovieListFragment의 ViewPager2에 필요한 영화 리스트 전달
     *
     * @param movieList 영화 리스트
     */
    private void showMovieListFragment(ArrayList<MovieInfo> movieList) {
        MovieListFragment listFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("movieList", movieList);
        listFragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.container, listFragment).commit();
    }

    /**
     * MovieDetailFragment의 UI 작업에 필요한 영화 상세 내용 전달
     *
     * @param movieDetail 영화 상세 내용
     * @param commentList 한줄평 리스트
     */
    private void showMovieDetailFragment(MovieDetail movieDetail, ArrayList<MovieComment> commentList) {
        Fragment movieDetailFragment = manager.getFragmentFactory().instantiate(getClassLoader(), MovieDetailFragment.class.getName());
        Bundle bundle = new Bundle();
        bundle.putParcelable("movieDetail", movieDetail);
        bundle.putParcelableArrayList("movieCommentList", commentList);
        movieDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, movieDetailFragment);
        transaction.addToBackStack(null); //MovieDetailFragment에서 뒤로가기 버튼을 눌렀을 때 MovieListFragment로 되돌아오기 위해 설정해줍니다.
        transaction.commit();
    }

    /**
     * 네트워크 통신이 시작되면 ProgressBar 로딩이 시작되고 데이터가 조회되면 종료됩니다.
     */
    private void showLoading() {
        Fragment loadingDialogFragment = (LoadingFragment) manager.getFragmentFactory().instantiate(getClassLoader(), LoadingFragment.class.getName());
        manager.beginTransaction().replace(R.id.container, loadingDialogFragment).commit();
    }
}