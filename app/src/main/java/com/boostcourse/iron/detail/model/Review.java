package com.boostcourse.iron.detail.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String userId;
    private String contents;
    private Long time;
    private float grade;
    private int resId;
    private int recommendCount;

    public Review(String userId, String contents, float grade, int resId, int recommendCount) {
        this.userId = userId;
        this.contents = contents;
        this.grade = grade;
        this.resId = resId;
        this.recommendCount = recommendCount;
        setTime();
    }

    protected Review(Parcel in) {
        userId = in.readString();
        contents = in.readString();
        time = in.readLong();
        grade = in.readFloat();
        resId = in.readInt();
        recommendCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(contents);
        dest.writeLong(time);
        dest.writeFloat(grade);
        dest.writeInt(resId);
        dest.writeInt(recommendCount);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime() {
        this.time = System.currentTimeMillis();
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getResourceId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    @Override
    public String toString() {
        return "Review{" +
                "userId='" + userId + '\'' +
                ", time='" + time + '\'' +
                ", contents='" + contents + '\'' +
                ", grade=" + grade +
                ", recommendCount=" + recommendCount +
                '}';
    }
}
