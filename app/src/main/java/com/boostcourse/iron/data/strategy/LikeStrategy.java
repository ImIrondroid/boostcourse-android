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

public class LikeStrategy implements Strategy {

    private final DatabaseManager databaseManager;

    public LikeStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        MovieDetail movieDetail = bundle.getParcelable("movieDetail");
        String id = bundle.getString("id");
        String likeyn = bundle.getString("likeyn");

        Map<String, String> paramsLike = new HashMap<>();
        paramsLike.put("id", id);
        paramsLike.put("likeyn", likeyn);

        return new GsonRequest<>(
                Request.Method.POST,
                VolleyHelper.getUrl(Directory.LIKE),
                MovieResult.class,
                paramsLike,
                response -> databaseManager.saveLikeDislike(movieDetail),
                listener::onError
        );
    }
}
