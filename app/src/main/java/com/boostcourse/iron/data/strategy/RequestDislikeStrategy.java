package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.DatabaseManager;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.ui.model.MovieResult;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.data.network.GsonRequest;
import com.boostcourse.iron.data.network.VolleyHelper;

import java.util.HashMap;
import java.util.Map;

public class RequestDislikeStrategy implements RequestStrategy {

    private final DatabaseManager databaseManager;

    public RequestDislikeStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        MovieDetail movieDetail = bundle.getParcelable("movieDetail");
        String id = bundle.getString("id");
        String dislikeyn = bundle.getString("dislikeyn");

        Map<String, String> paramsDislike = new HashMap<>();
        paramsDislike.put("id", id);
        paramsDislike.put("dislikeyn", dislikeyn);

        return new GsonRequest<>(
                VolleyHelper.getUrl(Directory.DISLIKE),
                MovieResult.class,
                paramsDislike,
                response -> databaseManager.saveLikeDislike(movieDetail),
                listener::onError
        );
    }
}
