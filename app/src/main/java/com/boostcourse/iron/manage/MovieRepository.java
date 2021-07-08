package com.boostcourse.iron.manage;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.model.MovieComment;
import com.boostcourse.iron.model.MovieCommentResult;
import com.boostcourse.iron.model.MovieDetail;
import com.boostcourse.iron.model.MovieDetailResult;
import com.boostcourse.iron.model.MovieInfo;
import com.boostcourse.iron.model.MovieInfoResult;
import com.boostcourse.iron.model.MovieResult;
import com.boostcourse.iron.network.Directory;
import com.boostcourse.iron.network.GsonRequest;
import com.boostcourse.iron.network.VolleyHelper;
import com.boostcourse.iron.util.NetworkUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity나 Fragment에서 사용하는 인터페이스가 네트워크 통신인지 데이터베이스 통신인지 모르게 데이터를 다룰 수 있도록 Repository클래스를 구현하였습니다.
 */
public class MovieRepository {

    public DatabaseManager databaseManager;
    public NetworkManager networkManager;
    private final Context context;

    public MovieRepository(Context context) {
        databaseManager = new DatabaseManager(context);
        networkManager = new NetworkManager(context);
        this.context = context;
    }

    public void loadMovieList(FinishListener listener) {
        List<MovieInfo> list = databaseManager.getMovieList();
        if (list.isEmpty()) {
            if (NetworkUtil.isInternetConnected(context)) {
                listener.onNext();
            } else {
                listener.onError();
            }
        } else {
            listener.onFinish(list);
        }
    }

    public void loadMovieList(Directory directory, Bundle bundle, FinishListener listener) {
        if (bundle == null) return;

        if (directory == Directory.MOVIE) {
            String type = bundle.getString("type");

            Map<String, String> params = new HashMap<>();
            params.put("type", type);

            Request<MovieInfoResult> request = new GsonRequest<>(
                    VolleyHelper.getUrl(Directory.MOVIE),
                    MovieInfoResult.class,
                    params,
                    response -> {
                        List<MovieInfo> movieList = response.getResult();
                        boolean isComplete = databaseManager.isMovieListInserted(movieList);
                        if (isComplete) {
                            listener.onFinish(movieList);
                        }
                    },
                    listener::onError
            );

            networkManager.addRequest(request);
        } else if (directory == Directory.DETAIL) {
            String movieId = bundle.getString("movieId");
            MovieDetail movieDetail = databaseManager.getMovieDetail(Integer.parseInt(movieId));

            if (movieDetail == null) {
                if (NetworkUtil.isInternetConnected(context)) {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", movieId);

                    Request<MovieDetailResult> request = new GsonRequest<>(
                            VolleyHelper.getUrl(Directory.DETAIL),
                            MovieDetailResult.class,
                            params,
                            response -> {
                                MovieDetail newDetail = response.getResult().get(0);
                                boolean isComplete = databaseManager.isDetailSaved(newDetail);
                                if (isComplete) {
                                    listener.onFinish();
                                }
                            },
                            listener::onError
                    );

                    networkManager.addRequest(request);
                } else {
                    listener.onError();
                }
            } else {
                listener.onFinish();
            }
        } else if (directory == Directory.COMMENT) {
            String id = bundle.getString("movieId");
            String limit = String.valueOf(20);

            List<MovieComment> list = databaseManager.getMovieCommentList(Integer.parseInt(id));
            if (list.isEmpty()) {
                if (NetworkUtil.isInternetConnected(context)) {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("limit", limit);

                    Request<MovieCommentResult> request = new GsonRequest<>(
                            VolleyHelper.getUrl(Directory.COMMENT),
                            MovieCommentResult.class,
                            params,
                            response -> {
                                List<MovieComment> newList = response.getResult();
                                boolean isComplete = databaseManager.isCommentListSaved(newList);
                                if (isComplete) {
                                    listener.onFinish();
                                }
                            },
                            listener::onError
                    );

                    networkManager.addRequest(request);
                } else {
                    listener.onError();
                }
            } else {
                boolean isComplete = databaseManager.isCommentListSaved(list);
                if (isComplete) {
                    listener.onFinish();
                }
            }
        } else if (directory == Directory.LIKE) {
            MovieDetail movieDetail = bundle.getParcelable("movieDetail");
            String id = bundle.getString("id");
            String likeyn = bundle.getString("likeyn");
            String dislikeyn = bundle.getString("dislikeyn");

            Map<String, String> paramsLike = new HashMap<>();
            paramsLike.put("id", id);
            paramsLike.put("likeyn", likeyn);

            Request<MovieResult> requestLike = new GsonRequest<>(
                    VolleyHelper.getUrl(Directory.LIKE),
                    MovieResult.class,
                    paramsLike,
                    response -> databaseManager.saveLikeDislike(movieDetail),
                    error -> listener.onError()
            );

            Map<String, String> paramsDislike = new HashMap<>();
            paramsDislike.put("id", id);
            paramsDislike.put("dislikeyn", dislikeyn);

            Request<MovieResult> requestDislike = new GsonRequest<>(
                    VolleyHelper.getUrl(Directory.LIKE),
                    MovieResult.class,
                    paramsDislike,
                    response -> databaseManager.saveLikeDislike(movieDetail),
                    error -> listener.onError()
            );

            networkManager.addRequest(requestLike);
            networkManager.addRequest(requestDislike);
        } else if (directory == Directory.RECOMMEND) {
            String review_id = bundle.getString("review_id");

            HashMap<String, String> params = new HashMap<>();
            params.put("review_id", review_id);

            Request<MovieResult> request = new GsonRequest<>(
                    VolleyHelper.getUrl(Directory.RECOMMEND),
                    MovieResult.class,
                    params,
                    response -> listener.onFinish(),
                    error -> listener.onError()
            );

            networkManager.addRequest(request);
        } else if (directory == Directory.COMMENTSEND) {
            String movieId = bundle.getString("movieId");
            String limit = bundle.getString("limit");

            Map<String, String> params = new HashMap<>();
            params.put("id", movieId);
            params.put("limit", limit);

            Request<MovieCommentResult> request = new GsonRequest<>(
                    VolleyHelper.getUrl(Directory.COMMENTSEND),
                    MovieCommentResult.class,
                    params,
                    response -> {
                        List<MovieComment> newList = response.getResult();
                        boolean isComplete = databaseManager.isCommentInserted(newList.get(0)); //방금 등록된 새로운 한줄평
                        if (isComplete) {
                            List<MovieComment> updatedList = databaseManager.getMovieCommentList(Integer.parseInt(movieId));
                            listener.onFinish(updatedList);
                        }
                    },
                    listener::onError
            );

            networkManager.addRequest(request);
        } else if (directory == Directory.CREATE) {
            String movieId = bundle.getString("movieId");
            String writer = bundle.getString("writer");
            String rating = bundle.getString("rating");
            String contents = bundle.getString("contents");

            Map<String, String> params = new HashMap<>();
            params.put("id", movieId);
            params.put("writer", writer);
            params.put("rating", rating);
            params.put("contents", contents);

            Request<MovieResult> request = new GsonRequest<>(
                    VolleyHelper.getUrl(Directory.CREATE),
                    MovieResult.class,
                    params,
                    response -> listener.onFinish(),
                    listener::onError
            );

            networkManager.addRequest(request);
        }
    }
}
