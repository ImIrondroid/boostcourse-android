package com.boostcourse.iron.manage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.boostcourse.iron.network.VolleyHelper;

/**
 * Network 처리를 지원해주는 Manager 클래스 입니다.
 */
public class NetworkManager {

    private static RequestQueue requestQueue;

    public NetworkManager(Context context) {
        requestQueue = VolleyHelper.getInstance(context).getRequestQueue();
    }

    public <T> void addRequest(Request<T> request) {
        requestQueue.add(request);
    }
}
