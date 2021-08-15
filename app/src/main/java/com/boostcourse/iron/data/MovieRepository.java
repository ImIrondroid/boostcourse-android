package com.boostcourse.iron.data;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.LiveData;

import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.data.network.RequestStrategy;
import com.boostcourse.iron.data.strategy.CommentCreateStrategy;
import com.boostcourse.iron.data.strategy.CommentListStrategy;
import com.boostcourse.iron.data.strategy.CreateStrategy;
import com.boostcourse.iron.data.strategy.DetailStrategy;
import com.boostcourse.iron.data.strategy.DislikeStrategy;
import com.boostcourse.iron.data.strategy.LikeStrategy;
import com.boostcourse.iron.data.strategy.MovieListStrategy;
import com.boostcourse.iron.data.strategy.RecommendStrategy;
import com.boostcourse.iron.util.NetworkUtil;

import java.util.List;

/**
 * Activity나 Fragment에서 사용하는 인터페이스가 네트워크 통신인지 데이터베이스 통신인지 모르게 데이터를 다룰 수 있도록 Repository클래스를 구현하였습니다.
 */
public class MovieRepository {

    private final Context context;
    private final DatabaseManager databaseManager;
    private final NetworkManager networkManager;

    public MovieRepository(Context context) {
        this.context = context;
        databaseManager = new DatabaseManager(context);
        networkManager = new NetworkManager(context);
    }

    public LiveData<List<MovieComment>> getMovieCommentList(int movieId) {
        return databaseManager.getMovieCommentListLiveData(movieId);
    }

    /**
     * Wifi 또는 Internet에 연결되었을 때랑 그렇지않을 때를 구분지었습니다.
     *
     * @param directory Request 타입
     * @param bundle 전달 데이터
     * @param listener 콜백 메서드
     */
    public void sendRequest(Directory directory, Bundle bundle, FinishListener listener) {
        if (bundle == null) return;

        if (NetworkUtil.isInternetConnected(context)) {
            RequestStrategy requestStrategy = new RequestStrategy();

            if (directory == Directory.MOVIE) {
                requestStrategy.setStrategy(new MovieListStrategy(databaseManager));
            } else if (directory == Directory.DETAIL) {
                requestStrategy.setStrategy(new DetailStrategy(databaseManager));
            } else if (directory == Directory.COMMENTLIST) {
                requestStrategy.setStrategy(new CommentListStrategy(databaseManager));
            } else if (directory == Directory.LIKE) {
                requestStrategy.setStrategy(new LikeStrategy(databaseManager));
            } else if (directory == Directory.DISLIKE) {
                requestStrategy.setStrategy(new DislikeStrategy(databaseManager));
            } else if (directory == Directory.RECOMMEND) {
                requestStrategy.setStrategy(new RecommendStrategy(databaseManager));
            } else if (directory == Directory.COMMENTCREATE) {
                requestStrategy.setStrategy(new CommentCreateStrategy(databaseManager));
            } else if (directory == Directory.CREATE) {
                requestStrategy.setStrategy(new CreateStrategy());
            }

            if (requestStrategy.isSet()) {
                networkManager.addRequest(requestStrategy.getRequest(bundle, listener));
            }
        } else {
            if (directory == Directory.MOVIE) {
                List<MovieInfo> list = databaseManager.getMovieList();
                if (!list.isEmpty()) {
                    listener.onFinish(list);
                } else {
                    listener.onError();
                }
            } else if (directory == Directory.DETAIL) {
                String movieId = bundle.getString("movieId");
                MovieDetail movieDetail = databaseManager.getMovieDetail(Integer.parseInt(movieId));
                if (movieDetail != null) {
                    listener.onFinish();
                } else {
                    listener.onError();
                }
            } else if (directory == Directory.COMMENTLIST) {
                String movieId = bundle.getString("movieId");
                List<MovieComment> list = databaseManager.getMovieCommentList(Integer.parseInt(movieId));
                if (!list.isEmpty()) {
                    listener.onFinish();
                } else {
                    listener.onError();
                }
            } else if (directory == Directory.LIKE) {
                listener.onError();
            } else if (directory == Directory.DISLIKE) {
                listener.onError();
            } else if (directory == Directory.RECOMMEND) {
                listener.onError();
            } else if (directory == Directory.COMMENTCREATE) {
                String movieId = bundle.getString("movieId");
                List<MovieComment> list = databaseManager.getMovieCommentList(Integer.parseInt(movieId));
                if (!list.isEmpty()) {
                    listener.onFinish(list);
                } else {
                    listener.onError();
                }
            } else if (directory == Directory.CREATE) {
                listener.onError();
            }
        }
    }
}
