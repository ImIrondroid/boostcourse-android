package com.boostcourse.iron.data;

import java.util.ArrayList;

public class MovieCommentResult {

    private String message;
    private int code;
    private String resultType;
    private ArrayList<MovieComment> result;

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

    public ArrayList<MovieComment> getResult() {
        return result;
    }

    public void setResult(ArrayList<MovieComment> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MovieCommentList{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", resultType='" + resultType + '\'' +
                ", result=" + result +
                '}';
    }
}
