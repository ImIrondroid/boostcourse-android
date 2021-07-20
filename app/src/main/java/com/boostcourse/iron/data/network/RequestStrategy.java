package com.boostcourse.iron.data.network;

import android.os.Bundle;

import com.android.volley.Request;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieResponse;

public class RequestStrategy {

   private com.boostcourse.iron.data.strategy.RequestStrategy requestStrategy;

   public boolean isSet() {
      return requestStrategy != null;
   }

   public void setStrategy(com.boostcourse.iron.data.strategy.RequestStrategy requestStrategy) {
      this.requestStrategy = requestStrategy;
   }

   public Request<? extends MovieResponse> getRequest(Bundle bundle, FinishListener listener) {
      return requestStrategy.newRequest(bundle, listener);
   }
}
