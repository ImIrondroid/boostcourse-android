package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.ui.model.MovieResult;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.data.network.GsonRequest;
import com.boostcourse.iron.data.network.VolleyHelper;

import java.util.HashMap;
import java.util.Map;

public class CreateStrategy implements Strategy {

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        String movieId = bundle.getString("movieId");
        String writer = bundle.getString("writer");
        String rating = bundle.getString("rating");
        String contents = bundle.getString("contents");

        Map<String, String> params = new HashMap<>();
        params.put("id", movieId);
        params.put("writer", writer);
        params.put("rating", rating);
        params.put("contents", contents);

        return new GsonRequest<>(
                Request.Method.POST,
                VolleyHelper.getUrl(Directory.CREATE),
                MovieResult.class,
                params,
                response -> listener.onFinish(),
                listener::onError
        );
    }
}
