package com.boostcourse.iron.data;

import java.util.ArrayList;

public class MovieInfoResult {

    private String message;
    private int code;
    private String resultType;
    private ArrayList<MovieInfo> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public ArrayList<MovieInfo> getResult() {
        return result;
    }

    public void setResult(ArrayList<MovieInfo> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MovieInfoResult{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", resultType='" + resultType + '\'' +
                ", result=" + result +
                '}';
    }
}
