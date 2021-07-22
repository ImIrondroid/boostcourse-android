package com.boostcourse.iron.ui.base;

import androidx.lifecycle.ViewModel;

import com.boostcourse.iron.data.MovieRepository;

public class BaseViewModel extends ViewModel {

    protected MovieRepository movieRepository;

    public BaseViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}
