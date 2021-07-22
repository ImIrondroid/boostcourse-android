package com.boostcourse.iron.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.model.MovieComment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    public static final int PREVIEW_ITEM_MAX_SIZE = 3; //MovieDetailFragment에서 보여질 아이템의 최대 개수

    private final LayoutInflater inflater;
    private CommentCallback callback;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final ArrayList<MovieComment> commentList = new ArrayList<>();

    public interface CommentCallback {
        void onClickedItemRecommend(int position);
    }

    public void setRecommendCallbackListener(CommentCallback callback) {
        this.callback = callback;
    }

    public CommentAdapter(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAll(ArrayList<MovieComment> list) {
        this.commentList.clear();
        this.commentList.addAll(list);
        notifyDataSetChanged();
    }

    public MovieComment getItem(int position) {
        return this.commentList.get(position);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        MovieComment comment = commentList.get(position);

        holder.tvUserId.setText(comment.getWriter());
        holder.tvUserContents.setText(comment.getContents());
        holder.tvUserRecommendCount.setText(String.valueOf(comment.getRecommend()));
        holder.tvUserCommentTimeDiff.setText(convertToFormatTime(comment.getTime()));
        holder.rbUserGrade.setRating(comment.getRating());

        holder.setCommentClickListener(callback);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
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
            if (milliseconds > 0L) {
                long rest = milliseconds / dayDivider;
                if (rest > 365) convertString = "오래";
                else if (rest > 0) convertString = rest + "일";
                else {
                    milliseconds %= dayDivider;
                    rest = milliseconds / hourDivider;
                    if (rest > 0) convertString = rest + "시간 ";

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

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        private CommentCallback callback;

        private TextView tvUserId;
        private TextView tvUserContents;
        private TextView tvUserRecommendCount;
        private TextView tvUserCommentTimeDiff;
        private View llUserRecommendGroup;
        private RatingBar rbUserGrade;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            onBind(itemView);
        }

        private void onBind(View itemView) {
            tvUserId = (TextView) itemView.findViewById(R.id.tv_user_id);
            tvUserContents = (TextView) itemView.findViewById(R.id.tv_user_contents);
            tvUserRecommendCount = (TextView) itemView.findViewById(R.id.tv_user_recommend_count);
            tvUserCommentTimeDiff = (TextView) itemView.findViewById(R.id.tv_user_comment_time_diff);
            llUserRecommendGroup = (View) itemView.findViewById(R.id.ll_user_recommend_group);
            rbUserGrade = (RatingBar) itemView.findViewById(R.id.rb_user_grade);

            llUserRecommendGroup.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (callback != null) {
                    callback.onClickedItemRecommend(position);
                }
            });
        }

        public void setCommentClickListener(CommentCallback callback) {
            this.callback = callback;
        }
    }
}
