package com.boostcourse.iron.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

public class AppFragmentFactory extends FragmentFactory {

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        if (MovieListFragment.class.getName().equals(className)) {
            return new MovieListFragment();
        } else if (MovieDetailFragment.class.getName().equals(className)) {
            return new MovieDetailFragment();
        } else if (LoadingFragment.class.getName().equals(className)) {
            return new LoadingFragment();
        }
        return super.instantiate(classLoader, className);
    }
}
