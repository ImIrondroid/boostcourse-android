package com.boostcourse.iron;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {

    private ArrayList<Review> items = new ArrayList<>();

    private final LayoutInflater inflater;
    private TextView tvUserId;
    private TextView tvUserContents;
    private TextView tvUserRecommendCount;
    private TextView tvUserReviewTimeDiff;
    private ImageView ivUserImage;
    private RatingBar rbUserGrade;

    public ReviewAdapter(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addReview(Review review) {
        items.add(review);
        notifyDataSetChanged();
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
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_review, parent, false);
        }

        tvUserId = (TextView) convertView.findViewById(R.id.tv_user_id);
        tvUserContents = (TextView) convertView.findViewById(R.id.tv_user_contents);
        tvUserRecommendCount = (TextView) convertView.findViewById(R.id.tv_user_recommend_count);
        tvUserReviewTimeDiff = (TextView) convertView.findViewById(R.id.tv_user_review_time_diff);
        ivUserImage = (ImageView) convertView.findViewById(R.id.iv_user_image);
        rbUserGrade = (RatingBar) convertView.findViewById(R.id.rb_user_grade);

        Review item = items.get(position);
        tvUserId.setText(item.getUserId());
        tvUserContents.setText(item.getContents());
        tvUserRecommendCount.setText(String.valueOf(item.getRecommendCount()));
        tvUserReviewTimeDiff.setText(String.valueOf((System.currentTimeMillis() - item.getTime()) / 60000));
        ivUserImage.setImageResource(item.getResourceId());
        rbUserGrade.setRating(item.getGrade());

        return convertView;
    }
}
