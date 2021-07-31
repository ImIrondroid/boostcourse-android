package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.DatabaseManager;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.ui.model.MovieResult;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.data.network.GsonRequest;
import com.boostcourse.iron.data.network.VolleyHelper;

import java.util.HashMap;

public class RequestRecommendStrategy implements RequestStrategy {

    private final DatabaseManager databaseManager;

    public RequestRecommendStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        String review_id = bundle.getString("review_id");
        MovieComment movieComment = bundle.getParcelable("movieComment");

        HashMap<String, String> params = new HashMap<>();
        params.put("review_id", review_id);

        return new GsonRequest<>(
                VolleyHelper.getUrl(Directory.RECOMMEND),
                MovieResult.class,
                params,
                response -> {
                    databaseManager.saveRecommend(movieComment);
                    listener.onFinish();
                },
                listener::onError
        );
    }
}
