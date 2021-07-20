package com.boostcourse.iron.data.database.mapper;

import com.boostcourse.iron.data.database.entity.MovieCommentEntity;
import com.boostcourse.iron.data.database.entity.MovieDetailEntity;
import com.boostcourse.iron.data.database.entity.MovieEntity;
import com.boostcourse.iron.ui.model.MovieComment;
import com.boostcourse.iron.ui.model.MovieDetail;
import com.boostcourse.iron.ui.model.MovieInfo;

public class MovieMapper {

    public static MovieComment mapToModel(MovieCommentEntity comment) {
        return new MovieComment(
                comment.getId(),
                comment.getWriter(),
                comment.getMovieId(),
                comment.getWriter_image(),
                comment.getTime(),
                comment.getTimestamp(),
                comment.getRating(),
                comment.getContents(),
                comment.getRecommend()
        );
    }

    public static MovieCommentEntity mapToEntity(MovieComment comment) {
        return new MovieCommentEntity(
                comment.getId(),
                comment.getWriter(),
                comment.getMovieId(),
                comment.getWriter_image(),
                comment.getTime(),
                comment.getTimestamp(),
                comment.getRating(),
                comment.getContents(),
                comment.getRecommend()
        );
    }

    public static MovieDetailEntity mapToEntity(MovieDetail detail) {
        return new MovieDetailEntity(
                detail.getId(),
                detail.getTitle(),
                detail.getTitle_eng(),
                detail.getDate(),
                detail.getUser_rating(),
                detail.getAudience_rating(),
                detail.getReview_rating(),
                detail.getReservation_rate(),
                detail.getReservation_grade(),
                detail.getGrade(),
                detail.getThumb(),
                detail.getImage(),
                detail.getPhotos(),
                detail.getVideos(),
                detail.getOutlinks(),
                detail.getGenre(),
                detail.getDuration(),
                detail.getAudience(),
                detail.getSynopsis(),
                detail.getDirector(),
                detail.getActor(),
                detail.getLike(),
                detail.isLiked(),
                detail.getDislike(),
                detail.isDisliked()
        );
    }

    public static MovieDetail mapToModel(MovieDetailEntity detail) {
        return new MovieDetail(
                detail.getId(),
                detail.getTitle(),
                detail.getTitle_eng(),
                detail.getDate(),
                detail.getUser_rating(),
                detail.getAudience_rating(),
                detail.getReview_rating(),
                detail.getReservation_rate(),
                detail.getReservation_grade(),
                detail.getGrade(),
                detail.getThumb(),
                detail.getImage(),
                detail.getPhotos(),
                detail.getVideos(),
                detail.getOutlinks(),
                detail.getGenre(),
                detail.getDuration(),
                detail.getAudience(),
                detail.getSynopsis(),
                detail.getDirector(),
                detail.getActor(),
                detail.getLike(),
                detail.isLiked(),
                detail.getDislike(),
                detail.isDisliked()
        );
    }

    public static MovieEntity mapToEntity(MovieInfo info) {
        return new MovieEntity(
                info.getId(),
                info.getTitle(),
                info.getDate(),
                info.getTitle_eng(),
                info.getUser_rating(),
                info.getAudience_rating(),
                info.getReview_rating(),
                info.getReservation_rate(),
                info.getReservation_grade(),
                info.getGrade(),
                info.getThumb(),
                info.getImage()
        );
    }

    public static MovieInfo mapToModel(MovieEntity movieEntity) {
        return new MovieInfo(
                movieEntity.getId(),
                movieEntity.getTitle(),
                movieEntity.getDate(),
                movieEntity.getTitle_eng(),
                movieEntity.getUser_rating(),
                movieEntity.getAudience_rating(),
                movieEntity.getReview_rating(),
                movieEntity.getReservation_rate(),
                movieEntity.getReservation_grade(),
                movieEntity.getGrade(),
                movieEntity.getThumb(),
                movieEntity.getImage()
        );
    }
}
