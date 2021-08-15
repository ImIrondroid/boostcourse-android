package com.boostcourse.iron.data.network;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.data.strategy.Strategy;
import com.boostcourse.iron.ui.model.MovieResponse;

public class RequestStrategy {

   private Strategy requestStrategy;

   public boolean isSet() {
      return requestStrategy != null;
   }

   public void setStrategy(Strategy requestStrategy) {
      this.requestStrategy = requestStrategy;
   }

   public Request<? extends MovieResponse> getRequest(Bundle bundle, FinishListener listener) {
      return requestStrategy.newRequest(bundle, listener);
   }
}
