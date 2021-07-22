package com.boostcourse.iron.ui;

import android.os.Bundle;

import androidx.lifecycle.LiveData;

import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.data.MovieRepository;
import com.boostcourse.iron.ui.base.BaseViewModel;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.data.Directory;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MovieViewModel extends BaseViewModel {

    @Inject
    public MovieViewModel(MovieRepository movieRepository) {
        super(movieRepository);
    }

    public void sendRequest(Directory directory, Bundle bundle, FinishListener listener) {
        movieRepository.sendRequest(directory, bundle, listener);
    }

    public LiveData<List<MovieComment>> getMovieCommentList(int movieId) {
        return movieRepository.getMovieCommentList(movieId);
    }
}
