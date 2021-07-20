package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.DatabaseManager;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieInfo;
import com.boostcourse.iron.ui.model.MovieInfoResult;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.data.network.GsonRequest;
import com.boostcourse.iron.data.network.VolleyHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestListStrategy implements RequestStrategy {

    private final DatabaseManager databaseManager;

    public RequestListStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        String type = bundle.getString("type");

        Map<String, String> params = new HashMap<>();
        params.put("type", type);

        return new GsonRequest<>(
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
    }
}
