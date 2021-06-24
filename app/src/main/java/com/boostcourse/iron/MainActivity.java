package com.boostcourse.iron;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.boostcourse.iron.callback.FragmentCallback;
import com.boostcourse.iron.detail.start.MovieDetailFragment;
import com.boostcourse.iron.list.model.Movie;
import com.boostcourse.iron.list.start.MovieListFragment;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    private DrawerLayout drawer;

    private FragmentManager manager;
    private MovieListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.nav_list) {
            if(manager.getBackStackEntryCount() > 0) onBackPressed(); //MovieDetailFragment이 보이는 상태일 때만 MovieListFragment로 돌아오기가 가능합니다.
        } else if(itemId == R.id.nav_api) {
            Toast.makeText(this, R.string.menu_api, Toast.LENGTH_SHORT).show();
        } else if(itemId == R.id.nav_book) {
            Toast.makeText(this, R.string.menu_book, Toast.LENGTH_SHORT).show();
        } else if(itemId == R.id.nav_settings) {
            Toast.makeText(this, R.string.menu_user_settings, Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);

        return false;
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

        listFragment = new MovieListFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, listFragment).commit();
    }

    @Override
    public void onClickedViewsDetailButton(Movie movie) {
        Fragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); //MovieDetailFragment에서 뒤로가기 버튼을 눌렀을 때 MovieListFragment로 되돌아오기 위해 설정해줍니다.
        transaction.commit();
    }
}