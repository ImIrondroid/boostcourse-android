package com.boostcourse.iron.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.databinding.FragmentMovieDetailBinding;
import com.boostcourse.iron.ui.adapter.CommentListAdapter;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieGallery;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.ui.FragmentCallback;
import com.boostcourse.iron.ui.adapter.GalleryRecyclerViewAdapter;
import com.boostcourse.iron.ui.MovieViewModel;
import com.boostcourse.iron.ui.activity.CommentSeeActivity;
import com.boostcourse.iron.ui.activity.CommentWriteActivity;
import com.boostcourse.iron.ui.activity.GalleryActivity;
import com.boostcourse.iron.ui.base.BaseFragment;
import com.bumptech.glide.Glide;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailFragment extends BaseFragment<MovieViewModel, FragmentMovieDetailBinding> implements CommentListAdapter.CommentListCallback {

    private GalleryRecyclerViewAdapter galleryRecyclerViewAdapter;
    private CommentListAdapter commentListAdapter;
    private MovieDetail movieDetail;
    private FragmentCallback callback;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_movie_detail;
    }

    @Override
    protected Class<MovieViewModel> getViewModelClazz() {
        return MovieViewModel.class;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (callback != null) {
            callback = null;
        }
    }

    @Override
    public void init() {
        super.init();

        Bundle bundle = getArguments();
        if (bundle != null) {
            int movieId = bundle.getInt("movieId", Integer.MIN_VALUE);
            if (movieId != Integer.MIN_VALUE) {
                loadMovieDetail(movieId);
                loadMovieCommentList(movieId);
            }
        }

        binding.ivMovieLike.setOnClickListener(view -> {
            updateLike();
            applyOnServer(Directory.LIKE);
        });

        binding.ivMovieDislike.setOnClickListener(view -> {
            updateDislike();
            applyOnServer(Directory.DISLIKE);
        });

        binding.tvCommentSee.setOnClickListener(view -> { //?????? ?????? ???????????? ?????? ???????????? ?????? ???????????????.
            Intent intent = new Intent(requireActivity(), CommentSeeActivity.class);
            if (movieDetail != null) {
                intent.putExtra("movieDetail", movieDetail);
            }
            startActivity(intent);
        });

        binding.tvCommentWrite.setOnClickListener(view -> { //???????????? ???????????? ???????????????.
            Intent intent = new Intent(requireActivity(), CommentWriteActivity.class);
            if (movieDetail != null) {
                intent.putExtra("movieId", movieDetail.getId()); //????????? ????????? ?????? ?????? ID ????????????
            }
            startActivity(intent);
        });
    }

    private void setMovieDetail(MovieDetail movieDetail) {
        binding.setDetail(movieDetail);

        galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(requireActivity());
        galleryRecyclerViewAdapter.addPhoto(movieDetail.getPhotos());
        galleryRecyclerViewAdapter.addVideo(movieDetail.getVideos());
        if (galleryRecyclerViewAdapter.getItemCount() != 0) { //????????? ???????????? ???????????? ???????????? ?????? ??????
            binding.tvMovieNoGallery.setVisibility(View.INVISIBLE);
            galleryRecyclerViewAdapter.setGalleryCallback(this::showGalleryActivity);
            binding.rcvMovieGallery.setAdapter(galleryRecyclerViewAdapter);
        } else { //????????? ???????????? ???????????? ????????? ?????? ??????
            binding.tvMovieNoGallery.setVisibility(View.VISIBLE);
        }

        commentListAdapter = new CommentListAdapter(requireActivity());
        commentListAdapter.setRecommendCallbackListener(this);
        binding.rcvMovieComment.setAdapter(commentListAdapter);

        viewModel.getMovieCommentList(movieDetail.getId()).observe(getViewLifecycleOwner(), commentList -> {
            if (!commentList.isEmpty()) {
                commentListAdapter.submitList(commentList.subList(0, Math.min(commentList.size(), CommentListAdapter.PREVIEW_ITEM_MAX_SIZE)));
            }
        });
    }

    /**
     * ???????????? ????????? ??? UI ??? ???????????? ???????????? ??? ??? ????????? ???????????????.
     */
    private void updateLike() {
        if (movieDetail.isLiked()) {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_up).into(binding.ivMovieLike);
            movieDetail.setLike(movieDetail.getLike() - 1);
        } else {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_up_selected).into(binding.ivMovieLike);
            movieDetail.setLike(movieDetail.getLike() + 1);

            if (movieDetail.isDisliked()) {
                Glide.with(requireActivity()).load(R.drawable.ic_thumb_down).into(binding.ivMovieDislike);
                movieDetail.setDislike(movieDetail.getDislike() - 1);
                binding.tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));
                movieDetail.setDisliked(false);
            }
        }
        binding.tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));

        movieDetail.setLiked(!movieDetail.isLiked());
    }

    /**
     * ???????????? ????????? ??? UI ??? ???????????? ???????????? ??? ??? ????????? ???????????????.
     */
    private void updateDislike() {
        if (movieDetail.isDisliked()) {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_down).into(binding.ivMovieDislike);
            movieDetail.setDislike(movieDetail.getDislike() - 1);
        } else {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_down_selected).into(binding.ivMovieDislike);
            movieDetail.setDislike(movieDetail.getDislike() + 1);

            if (movieDetail.isLiked()) {
                Glide.with(requireActivity()).load(R.drawable.ic_thumb_up).into(binding.ivMovieLike);
                movieDetail.setLike(movieDetail.getLike() - 1);
                binding.tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));
                movieDetail.setLiked(false);
            }
        }
        binding.tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));

        movieDetail.setDisliked(!movieDetail.isDisliked());
    }

    /**
     * ???????????? ???????????? ????????? ????????? ???????????????.
     */
    private void applyOnServer(Directory directory) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movieDetail", movieDetail);
        bundle.putString("id", String.valueOf(movieDetail.getId()));

        if (directory == Directory.LIKE) {
            if (movieDetail.isLiked()) bundle.putString("likeyn", "Y");
            else bundle.putString("likeyn", "N");
        } else if (directory == Directory.DISLIKE) {
            if (movieDetail.isDisliked()) bundle.putString("dislikeyn", "Y");
            else bundle.putString("dislikeyn", "N");
        }

        viewModel.sendRequest(directory, bundle, new FinishListener() {
            @Override
            public void onError(Exception e) {
                Log.e("applyOnServer()", e.getMessage() != null ? e.getMessage() : getString(R.string.please_connect_internet));
            }

            @Override
            public void onError() {
                Log.e("applyOnServer()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * ????????? ???????????? ????????? ???????????? ?????? ????????? ?????????.
     * ???????????? ????????? ????????? ?????? ????????? ???????????? ????????? ?????? ????????? ???????????? UI??? ???????????? ?????????.
     *
     * @param position ????????? ???????????? ?????????
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
            public void onError(Exception e) {
                Log.e("onClickedRecommendItem()", e.getMessage() != null ? e.getMessage() : getString(R.string.please_connect_internet));
            }

            @Override
            public void onError() {
                Log.e("onClickedRecommendItem()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * MovieDetailFragment??? ????????? ?????? ?????? ?????? ??????
     *
     * @param movieId ?????? ?????????
     */
    private void loadMovieDetail(int movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));

        viewModel.sendRequest(Directory.DETAIL, bundle, new FinishListener() {
            @Override
            public void onFinish(MovieResponse result) {
                movieDetail = (MovieDetail) result;
                setMovieDetail(movieDetail);
            }

            @Override
            public void onError(Exception e) {
                Log.e("loadMovieDetail()", e.getMessage() != null ? e.getMessage() : getString(R.string.please_connect_internet));
            }

            @Override
            public void onError() {
                Log.e("loadMovieDetail()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * MovieDetailFragment??? ListView??? ????????? ?????? ????????? ????????? ??????
     *
     * @param movieId ?????? ?????????
     */
    private void loadMovieCommentList(int movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));
        bundle.putString("limit", String.valueOf(20));

        viewModel.sendRequest(Directory.COMMENTLIST, bundle, new FinishListener() {
            @Override
            public void onError(Exception e) {
                Log.e("loadMovieCommentList()", e.getMessage() != null ? e.getMessage() : getString(R.string.please_connect_internet));
            }
        });
    }

    private void showGalleryActivity(int position) {
        MovieGallery movieGallery = galleryRecyclerViewAdapter.getItem(position);
        Intent intent = new Intent(requireActivity(), GalleryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY); //Youtube ?????? ??? ???????????? ?????? ??????
        intent.putExtra("movieGallery", movieGallery);
        startActivity(intent);
    }
}
