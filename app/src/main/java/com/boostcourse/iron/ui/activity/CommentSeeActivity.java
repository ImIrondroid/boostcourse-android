package com.boostcourse.iron.ui.activity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.MovieViewModel;
import com.boostcourse.iron.ui.adapter.CommentAdapter;
import com.boostcourse.iron.ui.base.BaseActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentSeeActivity extends BaseActivity<MovieViewModel> implements CommentAdapter.CommentCallback {

    private CommentAdapter commentAdapter;
    private MovieDetail movieDetail;

    private RecyclerView rcvMovieComment;
    private RatingBar rbMovieUserRating;
    private ImageView ivMovieAgeLimit;
    private TextView tvMovieAudienceRating;
    private TextView tvMovieTitle;
    private TextView tvReviewWrite;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_comment_see;
    }

    @Override
    protected int getTitleRes() {
        return R.string.app_bar_comment_see_name;
    }

    @Override
    protected Class<MovieViewModel> getViewModelClazz() {
        return MovieViewModel.class;
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }

    @Override
    public void init() {
        super.init();

        rcvMovieComment = (RecyclerView) findViewById(R.id.rcv_movie_comment);
        rbMovieUserRating = (RatingBar) findViewById(R.id.rb_movie_user_rating);
        ivMovieAgeLimit = (ImageView) findViewById(R.id.iv_movie_age_limit);
        tvMovieAudienceRating = (TextView) findViewById(R.id.tv_movie_audience_rating);
        tvMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        tvReviewWrite = (TextView) findViewById(R.id.tv_comment_write);

        Intent intent = getIntent();
        if (intent != null) { //MovieDetailFragment에서 전달한 리뷰 리스트를 가져옵니다.
            movieDetail = intent.getParcelableExtra("movieDetail");
            if (movieDetail != null) {
                tvMovieTitle.setText(movieDetail.getTitle());
                tvMovieAudienceRating.setText(String.valueOf(movieDetail.getAudience_rating()));
                rbMovieUserRating.setRating(movieDetail.getUser_rating());

                int grade = movieDetail.getGrade();
                int gradeId = R.drawable.ic_all;
                if (grade == 12) {
                    gradeId = R.drawable.ic_12;
                } else if (grade == 15) {
                    gradeId = R.drawable.ic_15;
                } else if (grade == 19) {
                    gradeId = R.drawable.ic_19;
                }
                Glide.with(this).load(gradeId).into(ivMovieAgeLimit);

                commentAdapter = new CommentAdapter(this);
                commentAdapter.setRecommendCallbackListener(this);
                rcvMovieComment.setAdapter(commentAdapter);

                loadMovieCommentList();
            }
        }

        tvReviewWrite.setOnClickListener(view -> {
            Intent sendIntent = new Intent(this, CommentWriteActivity.class);
            sendIntent.putExtra("movieId", movieDetail.getId());
            startActivity(sendIntent);
        });
    }

    private void loadMovieCommentList() {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieDetail.getId()));

        viewModel.sendRequest(Directory.COMMENTLIST, bundle, new FinishListener() {
            @Override
            public void onFinish() {
                viewModel.getMovieCommentList(movieDetail.getId()).observe(CommentSeeActivity.this, new Observer<List<MovieComment>>() {
                    @Override
                    public void onChanged(List<MovieComment> commentList) {
                        commentAdapter.addAll(new ArrayList<>(commentList));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("loadMovieCommentList()", e.getMessage() != null ? e.getMessage() : getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * 한줄평 추천 문구 클릭시 발동하는 콜백 메소드 입니다.
     * 추천수를 서버에 저장한 후에 저장이 완료되면 정보를 다시 불러올 필요없이 UI를 업데이트 합니다.
     *
     * @param position 한줄평 리스트의 인덱스
     */
    @Override
    public void onClickedItemRecommend(int position) {
        MovieComment movieComment = (MovieComment) commentAdapter.getItem(position);
        movieComment.setRecommend(movieComment.getRecommend() + 1);

        Bundle bundle = new Bundle();
        bundle.putString("review_id", String.valueOf(movieComment.getId()));
        bundle.putParcelable("movieComment", movieComment);

        viewModel.sendRequest(Directory.RECOMMEND, bundle, new FinishListener() {
            @Override
            public void onError(Exception e) {
                Log.e("onClickedItemRecommend()", e.getMessage() != null ? e.getMessage() : getString(R.string.please_connect_internet));
            }

            @Override
            public void onError() {
                Log.e("onClickedItemRecommend()", getString(R.string.please_connect_internet));
            }
        });
    }
}