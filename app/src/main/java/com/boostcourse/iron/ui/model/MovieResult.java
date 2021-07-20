package com.boostcourse.iron.ui.model;

public class MovieResult extends MovieResponse {

    private String message;
    private int code;
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "message='" + message + '\'' +
                ", code=" + code + '\'' +
                ", result=" + result +
                '}';
    }
}
