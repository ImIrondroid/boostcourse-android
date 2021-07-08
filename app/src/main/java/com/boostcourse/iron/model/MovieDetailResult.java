package com.boostcourse.iron.model;

import java.util.ArrayList;

public class MovieDetailResult extends MovieResponse {

    private String message;
    private int code;
    private String resultType;
    private ArrayList<MovieDetail> result;

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

    public ArrayList<MovieDetail> getResult() {
        return result;
    }

    public void setResult(ArrayList<MovieDetail> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MovieDetailList{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", resultType='" + resultType + '\'' +
                ", result=" + result +
                '}';
    }
}
