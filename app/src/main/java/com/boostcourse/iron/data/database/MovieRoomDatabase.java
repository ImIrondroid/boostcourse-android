package com.boostcourse.iron.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.boostcourse.iron.data.database.dao.MovieCommentDao;
import com.boostcourse.iron.data.database.dao.MovieDao;
import com.boostcourse.iron.data.database.dao.MovieDetailDao;
import com.boostcourse.iron.data.database.entity.MovieCommentEntity;
import com.boostcourse.iron.data.database.entity.MovieDetailEntity;
import com.boostcourse.iron.data.database.entity.MovieEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MovieEntity.class, MovieDetailEntity.class, MovieCommentEntity.class}, version = 1, exportSchema = false)
public abstract class MovieRoomDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    public abstract MovieDetailDao movieDetailDao();

    public abstract MovieCommentDao movieCommentDao();

    private static volatile MovieRoomDatabase INSTANCE;
    private static final String DB_NAME = "movie_database";
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, DB_NAME)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}