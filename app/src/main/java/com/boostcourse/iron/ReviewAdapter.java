package com.boostcourse.iron;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {

    ArrayList<Review> items = new ArrayList<>();
    Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewItemView view;
        if(convertView == null) {
            view = new ReviewItemView(context);
        } else { //convertView가 null이 아닐때 새로운 View를 inflate 하는것이 아닌 기존에 inflate를 거친 View를 재사용하며 데이터만 바꾸는 작업을 진행합니다.
            view = (ReviewItemView) convertView;
        }

        Review item = items.get(position);
        view.setUserImage(R.drawable.user1);
        view.setUserId(item.userId);
        view.setContents(item.contents);
        view.setGrade(item.grade);
        view.setDiffTime(item.time);
        view.setRecommendCount(item.recommendCount);

        return view;
    }

    public void addReview(Review review) {
        items.add(review);
        notifyDataSetChanged();
    }
}
