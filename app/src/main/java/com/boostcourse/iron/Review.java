package com.boostcourse.iron;

public class Review {

    String userId;
    String contents;
    Long time;
    float grade;
    int resId;
    int recommendCount;

    public Review(String userId, String contents, float grade, int resId, int recommendCount) {
        this.userId = userId;
        this.contents = contents;
        this.grade = grade;
        this.resId = resId;
        this.recommendCount = recommendCount;
        setTime();
    }

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
