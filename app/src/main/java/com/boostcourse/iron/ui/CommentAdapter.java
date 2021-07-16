package com.boostcourse.iron.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.model.MovieComment;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private CommentCallback callback;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<MovieComment> commentList = new ArrayList<>();
    public static final int PREVIEW_ITEM_MAX_SIZE = 3; //MovieDetailFragment에서 보여질 아이템의 최대 개수

    private TextView tvUserId;
    private TextView tvUserContents;
    private TextView tvUserRecommendCount;
    private TextView tvUserCommentTimeDiff;
    private ImageView ivUserImage;
    private View llUserRecommendGroup;
    private RatingBar rbUserGrade;

    public interface CommentCallback {
        void onClickedItemRecommend(int position);
    }

    public void setRecommendCallbackListener(CommentCallback callback) {
        this.callback = callback;
    }

    public CommentAdapter(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addComment(MovieComment movieComment) {
        this.commentList.add(movieComment);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<MovieComment> items) {
        this.commentList.addAll(items);
        notifyDataSetChanged();
    }

    public void setMovieComment(int position, MovieComment movieComment) {
        this.commentList.set(position, movieComment);
        notifyDataSetChanged();
    }

    public void setMovieCommentList(ArrayList<MovieComment> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged(); //어댑터에게 데이터가 바뀌었다는 알림을 통해 다음 에러를 해결합니다. The content of the adapter has changed but ListView did not receive a notification.
    }

    public ArrayList<MovieComment> getMovieCommentList() {
        return this.commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentViewHolder holder;
        if (convertView == null) {
            holder = new CommentViewHolder();
            convertView = inflater.inflate(R.layout.item_comment, parent, false);

            tvUserId = (TextView) convertView.findViewById(R.id.tv_user_id);
            tvUserContents = (TextView) convertView.findViewById(R.id.tv_user_contents);
            tvUserRecommendCount = (TextView) convertView.findViewById(R.id.tv_user_recommend_count);
            tvUserCommentTimeDiff = (TextView) convertView.findViewById(R.id.tv_user_comment_time_diff);
            ivUserImage = (ImageView) convertView.findViewById(R.id.iv_user_image);
            llUserRecommendGroup = (View) convertView.findViewById(R.id.ll_user_recommend_group);
            rbUserGrade = (RatingBar) convertView.findViewById(R.id.rb_user_grade);

            holder.tvUserId = tvUserId;
            holder.tvUserContents = tvUserContents;
            holder.tvUserRecommendCount = tvUserRecommendCount;
            holder.tvUserCommentTimeDiff = tvUserCommentTimeDiff;
            holder.ivUserImage = ivUserImage;
            holder.llUserRecommendGroup = llUserRecommendGroup;
            holder.rbUserGrade = rbUserGrade;

            convertView.setTag(holder);
        } else {
            holder = (CommentViewHolder) convertView.getTag();
        }

        MovieComment movieComment = commentList.get(position);
        holder.tvUserId.setText(movieComment.getWriter());
        holder.tvUserContents.setText(movieComment.getContents());
        holder.tvUserRecommendCount.setText(String.valueOf(movieComment.getRecommend()));
        holder.tvUserCommentTimeDiff.setText(convertToFormatTime(movieComment.getTime()));
        //Glide.with(convertView.getContext()).load(movieComment.getWriter_image()).into(holder.ivUserImage);
        holder.rbUserGrade.setRating(movieComment.getRating());
        holder.llUserRecommendGroup.setOnClickListener(view -> {
            if(callback != null) callback.onClickedItemRecommend(position);
        });

        return convertView;
    }

    /**
     * 서버에 'YYYY-MM-DD' 형식으로 날짜가 저장되어있어 시간 차이가 정확하지 않습니다.
     *
     * @param time 날짜 형식의 String
     * @return H일 or H시간 M분 or M분
     */
    private String convertToFormatTime(String time) {
        String convertString = "";
        long dayDivider = 24 * 60 * 60 * 1000L;
        long hourDivider = 60 * 60 * 1000L;
        long minDivider = 60 * 1000L;
        try {
            Date d = format.parse(time);
            long milliseconds = d != null ? System.currentTimeMillis() - d.getTime() : 0;
            if(milliseconds > 0L) {
                long rest = milliseconds / dayDivider;
                if(rest > 365) convertString = "오래";
                else if(rest > 0) convertString = rest + "일";
                else {
                    milliseconds %= dayDivider;
                    rest = milliseconds / hourDivider;
                    if(rest > 0) convertString = rest + "시간 ";

                    milliseconds %= hourDivider;
                    rest = milliseconds / minDivider;
                    convertString += rest + "분";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertString.isEmpty() ? "오래" : convertString;
    }

    /**
     * ViewHolder 패턴을 추가 적용하면 findViewById의 호출을 줄일 수 있어 적용해 보았습니다.
     */
    static class CommentViewHolder {
        private TextView tvUserId;
        private TextView tvUserContents;
        private TextView tvUserRecommendCount;
        private TextView tvUserCommentTimeDiff;
        private ImageView ivUserImage;
        private View llUserRecommendGroup;
        private RatingBar rbUserGrade;
    }
}
