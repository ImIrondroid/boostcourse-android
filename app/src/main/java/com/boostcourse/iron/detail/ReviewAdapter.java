package com.boostcourse.iron.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.detail.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {

    private final LayoutInflater inflater;

    private ArrayList<Review> items = new ArrayList<>();
    public static final int ITEM_MAX_SIZE = 3; //ReviewDetailFragment에서 보여질 아이템의 최대 개수

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
        notifyDataSetChanged(); //어댑터에게 데이터가 바뀌었다는 알림을 통해 다음 에러를 해결합니다. The content of the adapter has changed but ListView did not receive a notification.
    }

    public void setReviewList(ArrayList<Review> items) {
        this.items = items;
        notifyDataSetChanged(); //어댑터에게 데이터가 바뀌었다는 알림을 통해 다음 에러를 해결합니다. The content of the adapter has changed but ListView did not receive a notification.
    }

    public ArrayList<Review> getReviewList() {
        return this.items;
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
        ReviewViewHolder holder;
        if (convertView == null) { //ViewHolder를 추가 적용하면 findViewById의 호출을 줄일 수 있어 적용해 보았습니다.
            holder = new ReviewViewHolder();
            convertView = inflater.inflate(R.layout.item_review, parent, false);

            tvUserId = (TextView) convertView.findViewById(R.id.tv_user_id);
            tvUserContents = (TextView) convertView.findViewById(R.id.tv_user_contents);
            tvUserRecommendCount = (TextView) convertView.findViewById(R.id.tv_user_recommend_count);
            tvUserReviewTimeDiff = (TextView) convertView.findViewById(R.id.tv_user_review_time_diff);
            ivUserImage = (ImageView) convertView.findViewById(R.id.iv_user_image);
            rbUserGrade = (RatingBar) convertView.findViewById(R.id.rb_user_grade);

            holder.tvUserId = tvUserId;
            holder.tvUserContents = tvUserContents;
            holder.tvUserRecommendCount = tvUserRecommendCount;
            holder.tvUserReviewTimeDiff = tvUserReviewTimeDiff;
            holder.ivUserImage = ivUserImage;
            holder.rbUserGrade = rbUserGrade;

            convertView.setTag(holder);
        } else {
            holder = (ReviewViewHolder) convertView.getTag();
        }

        Review item = items.get(position);
        holder.tvUserId.setText(item.getUserId());
        holder.tvUserContents.setText(item.getContents());
        holder.tvUserRecommendCount.setText(String.valueOf(item.getRecommendCount()));
        holder.tvUserReviewTimeDiff.setText(String.valueOf((System.currentTimeMillis() - item.getTime()) / 60000));
        holder.ivUserImage.setImageResource(item.getResourceId());
        holder.rbUserGrade.setRating(item.getGrade());

        return convertView;
    }

    static class ReviewViewHolder {
        private TextView tvUserId;
        private TextView tvUserContents;
        private TextView tvUserRecommendCount;
        private TextView tvUserReviewTimeDiff;
        private ImageView ivUserImage;
        private RatingBar rbUserGrade;
    }
}
