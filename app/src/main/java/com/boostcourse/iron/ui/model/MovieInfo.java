package com.boostcourse.iron.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Item of MovieInfo
 *
 * {
 * "id":1,
 * "title":"꾼",
 * "title_eng":"The Swindlers",
 * "date":"2017-11-22",
 * "user_rating":4,
 * "audience_rating":8.36,
 * "reviewer_rating":4.33,
 * "reservation_rate":61.69,
 * "reservation_grade":1,
 * "grade":15,
 * "thumb":"http://movie2.phinf.naver.net/20171107_251/1510033896133nWqxG_JPEG/movie_image.jpg?type=m99_141_2",
 * "image":"http://movie.phinf.naver.net/20171107_251/1510033896133nWqxG_JPEG/movie_image.jpg"
 * }
 */

public class MovieInfo extends MovieResponse implements Parcelable {

    private int id;
    private String title;
    private String title_eng;
    private String date;
    private float user_rating;
    private float audience_rating;
    private float review_rating;
    private float reservation_rate;
    private int reservation_grade;
    private int grade;
    private String thumb;
    private String image;

    public MovieInfo(int id, String title, String title_eng, String date, float user_rating, float audience_rating, float review_rating, float reservation_rate, int reservation_grade, int grade, String thumb, String image) {
        this.id = id;
        this.title = title;
        this.title_eng = title_eng;
        this.date = date;
        this.user_rating = user_rating;
        this.audience_rating = audience_rating;
        this.review_rating = review_rating;
        this.reservation_rate = reservation_rate;
        this.reservation_grade = reservation_grade;
        this.grade = grade;
        this.thumb = thumb;
        this.image = image;
    }


    protected MovieInfo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        title_eng = in.readString();
        date = in.readString();
        user_rating = in.readFloat();
        audience_rating = in.readFloat();
        review_rating = in.readFloat();
        reservation_rate = in.readFloat();
        reservation_grade = in.readInt();
        grade = in.readInt();
        thumb = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(title_eng);
        dest.writeString(date);
        dest.writeFloat(user_rating);
        dest.writeFloat(audience_rating);
        dest.writeFloat(review_rating);
        dest.writeFloat(reservation_rate);
        dest.writeInt(reservation_grade);
        dest.writeInt(grade);
        dest.writeString(thumb);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public void setTitle_eng(String title_eng) {
        this.title_eng = title_eng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public void setAudience_rating(float audience_rating) {
        this.audience_rating = audience_rating;
    }

    public float getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(float review_rating) {
        this.review_rating = review_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public void setReservation_rate(float reservation_rate) {
        this.reservation_rate = reservation_rate;
    }

    public int getReservation_grade() {
        return reservation_grade;
    }

    public void setReservation_grade(int reservation_grade) {
        this.reservation_grade = reservation_grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Creator<MovieInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", title_eng='" + title_eng + '\'' +
                ", date='" + date + '\'' +
                ", user_rating=" + user_rating +
                ", audience_rating=" + audience_rating +
                ", review_rating=" + review_rating +
                ", reservation_rate=" + reservation_rate +
                ", reservation_grade=" + reservation_grade +
                ", grade=" + grade +
                ", thumb='" + thumb + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
