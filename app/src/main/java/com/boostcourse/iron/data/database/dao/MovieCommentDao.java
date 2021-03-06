package com.boostcourse.iron.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.boostcourse.iron.data.database.entity.MovieCommentEntity;

import java.util.List;

@Dao
public interface MovieCommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(MovieCommentEntity comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentList(List<MovieCommentEntity> commentList);

    @Update
    void updateComment(MovieCommentEntity comment);

    @Update
    void updateCommentList(List<MovieCommentEntity> commentList);

    @Query("SELECT * FROM movie_comment_table WHERE movieId =:movieId ORDER BY id DESC LIMIT 20")
    List<MovieCommentEntity> selectCommentList(int movieId);

    @Query("SELECT * FROM movie_comment_table WHERE movieId =:movieId ORDER BY id DESC LIMIT 20")
    LiveData<List<MovieCommentEntity>> selectCommentListLiveData(int movieId);
}
