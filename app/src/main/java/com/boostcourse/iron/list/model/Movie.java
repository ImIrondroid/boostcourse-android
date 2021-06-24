package com.boostcourse.iron.list.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String name;
    private String genre;
    private String contents;
    private int resId;
    private int ageId;

    public Movie(String name, String genre, String contents, int resId, int ageId) {
        this.name = name;
        this.genre = genre;
        this.contents = contents;
        this.resId = resId;
        this.ageId = ageId;
    }

    protected Movie(Parcel in) {
        name = in.readString();
        genre = in.readString();
        contents = in.readString();
        resId = in.readInt();
        ageId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(genre);
        dest.writeString(contents);
        dest.writeInt(resId);
        dest.writeInt(ageId);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAgeId() {
        return ageId;
    }

    public void setAgeId(int age) {
        this.ageId = age;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", contents='" + contents +
                '}';
    }
}
