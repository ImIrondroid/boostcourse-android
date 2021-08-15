package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.DatabaseManager;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieDetailResult;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.data.network.GsonRequest;
import com.boostcourse.iron.data.network.VolleyHelper;

import java.util.HashMap;
import java.util.Map;

public class DetailStrategy implements Strategy {

    private DatabaseManager databaseManager;

    public DetailStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        String movieId = bundle.getString("movieId");

        Map<String, String> params = new HashMap<>();
        params.put("id", movieId);

        return new GsonRequest<>(
                Request.Method.POST,
                VolleyHelper.getUrl(Directory.DETAIL),
                MovieDetailResult.class,
                params,
                response -> {
                    MovieDetail newDetail = response.getResult().get(0);
                    boolean isComplete = databaseManager.isDetailSaved(newDetail);
                    if (isComplete) {
                        listener.onFinish(newDetail);
                    }
                },
                listener::onError
        );
    }
}
