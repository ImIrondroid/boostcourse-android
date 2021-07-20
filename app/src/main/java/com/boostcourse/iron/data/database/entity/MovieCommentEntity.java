package com.boostcourse.iron.data.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_comment_table")
public class MovieCommentEntity {

    @PrimaryKey
    private int id;
    private String writer;
    private int movieId;
    private String writer_image;
    private String time;
    private long timestamp;
    private float rating;
    private String contents;
    private int recommend;

    public MovieCommentEntity(int id, String writer, int movieId, String writer_image, String time, long timestamp, float rating, String contents, int recommend) {
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