package com.boostcourse.iron.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {
 * "id":8170,
 * "writer":"테스터",
 * "movieId":3,
 * "writer_image":null,
 * "time":"2021-05-26 15:32:22",
 * "timestamp":1622010741,
 * "rating":2.0,
 * "contents":"굿",
 * "recommend":0
 * }
 */

public class MovieComment extends MovieResponse implements Parcelable {

    private int id;
    private String writer;
    private int movieId;
    private String writer_image;
    private String time;
    private long timestamp;
    private float rating;
    private String contents;
    private int recommend;

    public MovieComment(int id, String writer, int movieId, String writer_image, String time, long timestamp, float rating, String contents, int recommend) {
        this.id = id;
        this.writer = writer;
        this.movieId = movieId;
        this.writer_image = writer_image;
        this.time = time;
        this.timestamp = timestamp;
        this.rating = rating;
        this.contents = contents;
        this.recommend = recommend;
    }

    protected MovieComment(Parcel in) {
        id = in.readInt();
        writer = in.readString();
        movieId = in.readInt();
        writer_image = in.readString();
        time = in.readString();
        timestamp = in.readLong();
        rating = in.readFloat();
        contents = in.readString();
        recommend = in.readInt();
    }

    public static final Creator<MovieComment> CREATOR = new Creator<MovieComment>() {
        @Override
        public MovieComment createFromParcel(Parcel in) {
            return new MovieComment(in);
        }

        @Override
        public MovieComment[] newArray(int size) {
            return new MovieComment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(writer);
        dest.writeInt(movieId);
        dest.writeString(writer_image);
        dest.writeString(time);
        dest.writeLong(timestamp);
        dest.writeFloat(rating);
        dest.writeString(contents);
        dest.writeInt(recommend);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getWriter_image() {
        return writer_image;
    }

    public void setWriter_image(String writer_image) {
        this.writer_image = writer_image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "MovieComment{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", movieId=" + movieId +
                ", writer_image='" + writer_image + '\'' +
                ", time='" + time + '\'' +
                ", timestamp=" + timestamp +
                ", rating=" + rating +
                ", contents='" + contents + '\'' +
                ", recommend=" + recommend +
                '}';
    }
}
