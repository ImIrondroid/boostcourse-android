package com.boostcourse.iron.ui;

import android.os.Bundle;

import androidx.lifecycle.LiveData;

import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.data.MovieRepository;
import com.boostcourse.iron.ui.base.BaseViewModel;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.data.Directory;

import java.util.List;

public class MovieViewModel extends BaseViewModel {

    public MovieViewModel(MovieRepository movieRepository) {
        super(movieRepository);
    }

    public void sendRequest(Directory directory, Bundle bundle, FinishListener listener) {
        movieRepository.sendRequest(directory, bundle, listener);
    }

    public LiveData<List<MovieInfo>> getMovieList() {
        return movieRepository.getMovieList();
    }

    public LiveData<MovieDetail> getMovieDetail(int movieId) {
        return movieRepository.getMovieDetail(movieId);
    }

    public LiveData<List<MovieComment>> getMovieCommentList(int movieId) {
        return movieRepository.getMovieCommentList(movieId);
    }
}
