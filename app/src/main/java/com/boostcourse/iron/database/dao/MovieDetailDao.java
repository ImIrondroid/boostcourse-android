package com.boostcourse.iron.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.boostcourse.iron.database.entity.MovieDetailEntity;

@Dao
public interface MovieDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDetail(MovieDetailEntity movieDetailEntity);

    @Update
    void updateDetail(MovieDetailEntity movieDetailEntity);

    @Query("SELECT * FROM movie_detail_table WHERE id =:movieId")
    MovieDetailEntity selectDetail(int movieId);
}
