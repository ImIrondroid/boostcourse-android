package com.boostcourse.iron.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.ui.adapter.CommentAdapter;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieGallery;
import com.boostcourse.iron.ui.model.MovieResponse;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.ui.FragmentCallback;
import com.boostcourse.iron.ui.adapter.GalleryAdapter;
import com.boostcourse.iron.ui.adapter.GalleryItemDecoration;
import com.boostcourse.iron.ui.MovieViewModel;
import com.boostcourse.iron.ui.activity.CommentSeeActivity;
import com.boostcourse.iron.ui.activity.CommentWriteActivity;
import com.boostcourse.iron.ui.activity.GalleryActivity;
import com.boostcourse.iron.ui.base.BaseFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailFragment extends BaseFragment<MovieViewModel> implements CommentAdapter.CommentCallback {

    private GalleryAdapter galleryAdapter;
    private CommentAdapter commentAdapter;
    private MovieDetail movieDetail;
    private FragmentCallback callback;

    private RecyclerView rcvMovieGallery;
    private RecyclerView rcvMovieComment;
    private ImageView ivMovieImage;
    private ImageView ivMovieAgeLimit;
    private ImageView ivMovieLike;
    private ImageView ivMovieDislike;
    private TextView tvMovieDate;
    private TextView tvMovieTitle;
    private TextView tvMovieGenre;
    private TextView tvMovieDuration;
    private TextView tvMovieRank;
    private TextView tvMovieReservationRate;
    private TextView tvMovieAudience;
    private TextView tvMovieSynopsis;
    private TextView tvMovieLikeCount;
    private TextView tvMovieDislikeCount;
    private TextView tvMovieNoGallery;
    private TextView tvSeeComment;
    private TextView tvWriteComment;
    private TextView tvMovieAudienceRating;
    private TextView tvMovieDirector;
    private TextView tvMovieActor;
    private RatingBar rbMovieUserRating;

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
    public void init(ViewGroup rootView) {
        super.init(rootView);

        rcvMovieGallery = (RecyclerView) rootView.findViewById(R.id.rcv_movie_gallery);
        rcvMovieComment = (RecyclerView) rootView.findViewById(R.id.rcv_movie_comment);
        ivMovieImage = (ImageView) rootView.findViewById(R.id.iv_movie_image);
        ivMovieAgeLimit = (ImageView) rootView.findViewById(R.id.iv_movie_age_limit);
        ivMovieLike = (ImageView) rootView.findViewById(R.id.iv_movie_like);
        ivMovieDislike = (ImageView) rootView.findViewById(R.id.iv_movie_dislike);
        tvMovieDate = (TextView) rootView.findViewById(R.id.tv_movie_date);
        tvMovieTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        tvMovieGenre = (TextView) rootView.findViewById(R.id.tv_movie_genre);
        tvMovieDuration = (TextView) rootView.findViewById(R.id.tv_movie_duration);
        tvMovieRank = (TextView) rootView.findViewById(R.id.tv_movie_rank);
        tvMovieReservationRate = (TextView) rootView.findViewById(R.id.tv_movie_reservation_rate);
        tvMovieAudience = (TextView) rootView.findViewById(R.id.tv_movie_audience);
        tvMovieSynopsis = (TextView) rootView.findViewById(R.id.tv_movie_synopsis);
        tvMovieLikeCount = (TextView) rootView.findViewById(R.id.tv_movie_like_count);
        tvMovieDislikeCount = (TextView) rootView.findViewById(R.id.tv_movie_dislike_count);
        tvMovieNoGallery = (TextView) rootView.findViewById(R.id.tv_movie_no_gallery);
        tvSeeComment = (TextView) rootView.findViewById(R.id.tv_comment_see);
        tvWriteComment = (TextView) rootView.findViewById(R.id.tv_comment_write);
        tvMovieAudienceRating = (TextView) rootView.findViewById(R.id.tv_movie_audience_rating);
        tvMovieDirector = (TextView) rootView.findViewById(R.id.tv_movie_director);
        tvMovieActor = (TextView) rootView.findViewById(R.id.tv_movie_actor);
        rbMovieUserRating = (RatingBar) rootView.findViewById(R.id.rb_movie_user_rating);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int movieId = bundle.getInt("movieId", Integer.MIN_VALUE);
            if (movieId != Integer.MIN_VALUE) {
                loadMovieDetail(movieId);
                loadMovieCommentList(movieId);
            }
        }

        ivMovieLike.setOnClickListener(view -> {
            updateLike();
            applyOnServer(Directory.LIKE);
        });

        ivMovieDislike.setOnClickListener(view -> {
            updateDislike();
            applyOnServer(Directory.DISLIKE);
        });

        tvSeeComment.setOnClickListener(view -> { //모두 보기 화면으로 리뷰 리스트와 함께 전환합니다.
            Intent intent = new Intent(requireActivity(), CommentSeeActivity.class);
            if (movieDetail != null) {
                intent.putExtra("movieDetail", movieDetail);
            }
            startActivity(intent);
        });

        tvWriteComment.setOnClickListener(view -> { //작성하기 화면으로 전환합니다.
            Intent intent = new Intent(requireActivity(), CommentWriteActivity.class);
            if (movieDetail != null) {
                intent.putExtra("movieId", movieDetail.getId()); //한줄평 작성을 위해 영화 ID 전달하기
            }
            startActivity(intent);
        });
    }

    private void setMovieDetail(MovieDetail movieDetail) {
        tvMovieTitle.setText(movieDetail.getTitle());
        tvMovieDate.setText(movieDetail.getDate());
        tvMovieGenre.setText(movieDetail.getGenre());
        tvMovieDuration.setText(String.valueOf(movieDetail.getDuration()));
        tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));
        tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));
        tvMovieRank.setText(String.valueOf(movieDetail.getId()));
        tvMovieReservationRate.setText(String.valueOf(movieDetail.getReservation_rate()));
        rbMovieUserRating.setRating(movieDetail.getUser_rating());
        tvMovieAudienceRating.setText(String.valueOf(movieDetail.getAudience_rating()));
        tvMovieAudience.setText(movieDetail.getAudience());
        tvMovieSynopsis.setText(movieDetail.getSynopsis());
        tvMovieDirector.setText(movieDetail.getDirector());
        tvMovieActor.setText(movieDetail.getActor());

        if (movieDetail.isLiked())
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_up_selected).into(ivMovieLike);
        if (movieDetail.isDisliked())
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_down_selected).into(ivMovieDislike);

        int grade = movieDetail.getGrade();
        int gradeId = R.drawable.ic_all;
        if (grade == 12) {
            gradeId = R.drawable.ic_12;
        } else if (grade == 15) {
            gradeId = R.drawable.ic_15;
        } else if (grade == 19) {
            gradeId = R.drawable.ic_19;
        }
        Glide.with(requireActivity()).load(gradeId).into(ivMovieAgeLimit);
        Glide.with(requireActivity()).load(movieDetail.getThumb()).placeholder(R.drawable.image_not_available).into(ivMovieImage);

        galleryAdapter = new GalleryAdapter(requireActivity());
        galleryAdapter.addPhoto(movieDetail.getPhotos());
        galleryAdapter.addVideo(movieDetail.getVideos());
        if (galleryAdapter.getItemCount() != 0) { //영화의 사진이나 동영상이 한개라도 있는 경우
            tvMovieNoGallery.setVisibility(View.INVISIBLE);
            galleryAdapter.setGalleryCallback(this::showGalleryActivity);
            rcvMovieGallery.addItemDecoration(new GalleryItemDecoration(getActivity()));
            rcvMovieGallery.setAdapter(galleryAdapter);
        } else { //영화의 사진이나 동영상이 한개도 없는 경우
            tvMovieNoGallery.setVisibility(View.VISIBLE);
        }

        commentAdapter = new CommentAdapter(requireActivity());
        commentAdapter.setRecommendCallbackListener(this);
        rcvMovieComment.setAdapter(commentAdapter);

        viewModel.getMovieCommentList(movieDetail.getId()).observe(getViewLifecycleOwner(), commentList -> {
            if (!commentList.isEmpty()) {
                commentAdapter.addAll(new ArrayList<>(commentList.subList(0, Math.min(commentList.size(), CommentAdapter.PREVIEW_ITEM_MAX_SIZE))));
            }
        });
    }

    /**
     * 좋아요가 눌렀을 때 UI 및 데이터를 업데이트 한 후 서버에 저장합니다.
     */
    private void updateLike() {
        if (movieDetail.isLiked()) {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_up).into(ivMovieLike);
            movieDetail.setLike(movieDetail.getLike() - 1);
        } else {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_up_selected).into(ivMovieLike);
            movieDetail.setLike(movieDetail.getLike() + 1);

            if (movieDetail.isDisliked()) {
                Glide.with(requireActivity()).load(R.drawable.ic_thumb_down).into(ivMovieDislike);
                movieDetail.setDislike(movieDetail.getDislike() - 1);
                tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));
                movieDetail.setDisliked(false);
            }
        }
        tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));

        movieDetail.setLiked(!movieDetail.isLiked());
    }

    /**
     * 싫어요가 눌렀을 때 UI 및 데이터를 업데이트 한 후 서버에 저장합니다.
     */
    private void updateDislike() {
        if (movieDetail.isDisliked()) {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_down).into(ivMovieDislike);
            movieDetail.setDislike(movieDetail.getDislike() - 1);
        } else {
            Glide.with(requireActivity()).load(R.drawable.ic_thumb_down_selected).into(ivMovieDislike);
            movieDetail.setDislike(movieDetail.getDislike() + 1);

            if (movieDetail.isLiked()) {
                Glide.with(requireActivity()).load(R.drawable.ic_thumb_up).into(ivMovieLike);
                movieDetail.setLike(movieDetail.getLike() - 1);
                tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));
                movieDetail.setLiked(false);
            }
        }
        tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));

        movieDetail.setDisliked(!movieDetail.isDisliked());
    }

    /**
     * 좋아요나 싫어요가 눌리면 서버에 저장합니다.
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

        callback.sendRequestOnFragment(directory, bundle, new FinishListener() {
            @Override
            public void onError(Exception e) {
                Log.e("applyOnServer()", e.getMessage());
            }

            @Override
            public void onError() {
                Log.e("applyOnServer()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * 한줄평 추천문구 클릭시 발동하는 콜백 메소드 입니다.
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

        callback.sendRequestOnFragment(Directory.RECOMMEND, bundle, new FinishListener() {
            @Override
            public void onError(Exception e) {
                Log.e("onClickedRecommendItem()", e.getMessage());
            }

            @Override
            public void onError() {
                Log.e("onClickedRecommendItem()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * MovieDetailFragment에 필요한 영화 상세 내용 조회
     *
     * @param movieId 영화 아이디
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
                Log.e("loadMovieDetail()", e.getMessage());
            }

            @Override
            public void onError() {
                Log.e("loadMovieDetail()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * MovieDetailFragment의 ListView에 필요한 영화 한줄평 리스트 조회
     *
     * @param movieId 영화 아이디
     */
    private void loadMovieCommentList(int movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));
        bundle.putString("limit", String.valueOf(20));

        viewModel.sendRequest(Directory.COMMENTLIST, bundle, new FinishListener() {
            @Override
            public void onError(Exception e) {
                Log.e("loadMovieCommentList()", e.getMessage());
            }
        });
    }

    private void showGalleryActivity(int position) {
        MovieGallery movieGallery = galleryAdapter.getItem(position);
        Intent intent = new Intent(requireActivity(), GalleryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY); //Youtube 실행 후 뒤로가기 처리 목적
        intent.putExtra("movieGallery", movieGallery);
        startActivity(intent);
    }
}
