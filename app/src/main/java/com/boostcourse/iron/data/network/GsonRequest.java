package com.boostcourse.iron.data.network;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {

    private final Gson gson;
    private final Class<T> clazz;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;

    public GsonRequest(String url, Class<T> clazz, Map<String, String> params,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        this.params = params;
        this.listener = listener;
        gson = new Gson();

        setShouldCache(false);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    /**
     * Gson Library를 이용하여 Json 포맷의 response를 Gson 포맷으로 반환합니다.
     *
     * @param response 요청에 대한 Json 포맷의 응답
     * @return Gson 포맷의 응답
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
