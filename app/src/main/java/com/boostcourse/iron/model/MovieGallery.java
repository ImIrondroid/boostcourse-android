package com.boostcourse.iron.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieGallery extends MovieResponse implements Parcelable {

    private int id;
    private String path;

    public MovieGallery(int id, String path) {
        this.id = id;
        this.path = path;
    }

    protected MovieGallery(Parcel in) {
        id = in.readInt();
        path = in.readString();
    }

    public static final Creator<MovieGallery> CREATOR = new Creator<MovieGallery>() {
        @Override
        public MovieGallery createFromParcel(Parcel in) {
            return new MovieGallery(in);
        }

        @Override
        public MovieGallery[] newArray(int size) {
            return new MovieGallery[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public String getImagePath() {
        if(getId() == 1) { //photo image
            return getPath();
        } else { //video image
            String id = getPath().substring(17);
            return "https://img.youtube.com/vi/" + id + "/0.jpg";
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MovieGallery{" +
                "id=" + id +
                ", photo='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
    }
}
