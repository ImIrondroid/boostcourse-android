package com.boostcourse.iron.data.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.boostcourse.iron.data.Directory;

public class VolleyHelper {

    private static VolleyHelper instance;
    private RequestQueue requestQueue;

    private static final String protocol = "http://";
    private static final String domain = "boostcourse-appapi.connect.or.kr";
    private static final int port = 10000;
    public static final int RESPONSE_CODE = 200;

    private final Context context;

    private VolleyHelper(Context context) {
        this.context = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyHelper(context);
        }
        return instance;
    }

    public <T> void addRequest(Request<T> request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static String getUrl(Directory type) {
        String directory = "";
        if (type == Directory.MOVIE) directory += "/movie/readMovieList";
        else if (type == Directory.DETAIL) directory += "/movie/readMovie";
        else if (type == Directory.COMMENTLIST || type == Directory.COMMENTCREATE) directory += "/movie/readCommentList";
        else if (type == Directory.CREATE) directory += "/movie/createComment";
        else if (type == Directory.RECOMMEND) directory += "/movie/increaseRecommend";
        else if (type == Directory.LIKE || type == Directory.DISLIKE) directory += "/movie/increaseLikeDisLike";

        return protocol + domain + ":" + port + directory;
    }
}
