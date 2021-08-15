package com.boostcourse.iron.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.databinding.ActivityCommentSeeBinding;
import com.boostcourse.iron.ui.adapter.CommentListAdapter;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.MovieViewModel;
import com.boostcourse.iron.ui.base.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentSeeActivity extends BaseActivity<MovieViewModel, ActivityCommentSeeBinding> implements CommentListAdapter.CommentListCallback {

    private CommentListAdapter commentListAdapter;
    private MovieDetail movieDetail;

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

        Intent intent = getIntent();
        if (intent != null) { //MovieDetailFragment에서 전달한 리뷰 리스트를 가져옵니다.
            movieDetail = intent.getParcelableExtra("movieDetail");
            if (movieDetail != null) {
                binding.setDetail(movieDetail);

                commentListAdapter = new CommentListAdapter(this);
                commentListAdapter.setRecommendCallbackListener(this);
                binding.rcvMovieComment.setAdapter(commentListAdapter);

                viewModel.getMovieCommentList(movieDetail.getId()).observe(this, commentList -> {
                    if (!commentList.isEmpty()) {
                        commentListAdapter.submitList(commentList.subList(0, 20));
                    }
                });

                loadMovieCommentList();
            }
        }

        binding.tvCommentWrite.setOnClickListener(view -> {
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
        MovieComment movieComment = commentListAdapter.getCurrentList().get(position);
        movieComment.setRecommend(movieComment.getRecommend() + 1);

        Bundle bundle = new Bundle();
        bundle.putString("review_id", String.valueOf(movieComment.getId()));
        bundle.putParcelable("movieComment", movieComment);

        viewModel.sendRequest(Directory.RECOMMEND, bundle, new FinishListener() {
            @Override
            public void onFinish() {
                commentListAdapter.notifyItemChanged(position);
            }

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