package com.boostcourse.iron.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.boostcourse.iron.database.entity.MovieCommentEntity;
import com.boostcourse.iron.database.entity.MovieDetailEntity;
import com.boostcourse.iron.database.entity.MovieEntity;
import com.boostcourse.iron.model.MovieComment;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieList(List<MovieEntity> list);

    @Query("SELECT * FROM movie_table")
    List<MovieEntity> selectMovieList();
}
