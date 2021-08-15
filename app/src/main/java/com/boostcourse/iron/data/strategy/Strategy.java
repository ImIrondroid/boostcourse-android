package com.boostcourse.iron.data.strategy;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieResponse;

public interface Strategy {

    Request<? extends MovieResponse> newRequest(Bundle bundle, FinishListener listener);
}