package com.boostcourse.iron.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.boostcourse.iron.data.database.entity.MovieEntity;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieList(List<MovieEntity> list);

    @Query("SELECT * FROM movie_table")
    List<MovieEntity> selectMovieList();
}
