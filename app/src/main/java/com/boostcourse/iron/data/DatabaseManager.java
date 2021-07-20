package com.boostcourse.iron.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.boostcourse.iron.data.database.MovieRoomDatabase;
import com.boostcourse.iron.data.database.mapper.MovieMapper;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Database 작업을 처리하는 Manager 클래스 입니다.
 */
public class DatabaseManager {

    private final MovieRoomDatabase database;

    public DatabaseManager(Context context) {
        database = MovieRoomDatabase.getDatabase(context);
    }

    public LiveData<MovieDetail> getMovieDetailLiveData(int movieId) {
        return Transformations.map(
                database.movieDetailDao().selectDetailLiveData(movieId),
                MovieMapper::mapToModel
        );
    }

    public LiveData<List<MovieInfo>> getMovieListLiveData() {
        return Transformations.map(
                database.movieDao().selectMovieListLiveData(),
                list -> list.stream()
                        .map(MovieMapper::mapToModel)
                        .collect(Collectors.toList())
        );
    }

    public LiveData<List<MovieComment>> getMovieCommentListLiveData(int movieId) {
        return Transformations.map(
                database.movieCommentDao().selectCommentListLiveData(movieId),
                list -> list.stream()
                        .map(MovieMapper::mapToModel)
                        .collect(Collectors.toList())
        );
    }

    public List<MovieInfo> getMovieList() {
        Future<List<MovieInfo>> future = MovieRoomDatabase.executorService.submit(() -> database.movieDao().selectMovieList().stream()
                .map(MovieMapper::mapToModel)
                .collect(Collectors.toList())
        );
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }

    public MovieDetail getMovieDetail(int movieId) {
        Future<MovieDetail> future = MovieRoomDatabase.executorService.submit(() -> MovieMapper.mapToModel(database.movieDetailDao().selectDetail(movieId)));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    public List<MovieComment> getMovieCommentList(int movieId) {
        Future<List<MovieComment>> future = MovieRoomDatabase.executorService.submit(() -> database.movieCommentDao().selectCommentList(movieId).stream()
                .map(MovieMapper::mapToModel)
                .collect(Collectors.toList()));
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }

    public Boolean isCommentInserted(MovieComment comment) {
        Future<Boolean> future = MovieRoomDatabase.executorService.submit(() -> {
            database.movieCommentDao().insertComment(MovieMapper.mapToEntity(comment));
            return true;
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    public boolean isMovieListInserted(List<MovieInfo> list) {
        Future<Boolean> future = MovieRoomDatabase.executorService.submit(() -> {
            database.movieDao().insertMovieList(list.stream()
                    .map(MovieMapper::mapToEntity)
                    .collect(Collectors.toList())
            );
            return true;
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    public boolean isDetailSaved(MovieDetail detail) {
        Future<Boolean> future = MovieRoomDatabase.executorService.submit(() -> {
            MovieRoomDatabase.executorService.execute(() ->
                    database.movieDetailDao().insertDetail(MovieMapper.mapToEntity(detail)));
            return true;
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    public boolean isCommentListSaved(List<MovieComment> list) {
        Future<Boolean> future = MovieRoomDatabase.executorService.submit(() -> {
            database.movieCommentDao().insertCommentList(list.stream()
                    .map(MovieMapper::mapToEntity)
                    .collect(Collectors.toList())
            );
            return true;
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    public void saveCommentList(List<MovieComment> commentList) {
        MovieRoomDatabase.executorService.execute(() -> database.movieCommentDao().updateCommentList(commentList.stream()
                .map(MovieMapper::mapToEntity)
                .collect(Collectors.toList())
        ));
    }

    public void saveRecommend(MovieComment movieComment) {
        MovieRoomDatabase.executorService.execute(() ->
                database.movieCommentDao().updateComment(MovieMapper.mapToEntity(movieComment)));
    }

    public void saveLikeDislike(MovieDetail movieDetail) {
        MovieRoomDatabase.executorService.execute(() ->
                database.movieDetailDao().updateDetail(MovieMapper.mapToEntity(movieDetail)));
    }
}