package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.DatabaseManager;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieCommentResult;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.data.network.GsonRequest;
import com.boostcourse.iron.data.network.VolleyHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListStrategy implements Strategy {

    private final DatabaseManager databaseManager;

    public CommentListStrategy(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener) {

        String id = bundle.getString("movieId");
        String limit = "20";

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("limit", limit);

        return new GsonRequest<>(
                Request.Method.POST,
                VolleyHelper.getUrl(Directory.COMMENTLIST),
                MovieCommentResult.class,
                params,
                response -> {
                    List<MovieComment> newList = response.getResult();
                    databaseManager.isCommentListSaved(newList);
                    boolean isComplete = databaseManager.isCommentListSaved(newList);
                },
                listener::onError
        );
    }
}
