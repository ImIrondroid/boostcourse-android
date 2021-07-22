package com.boostcourse.iron.di;

import android.content.Context;

import com.boostcourse.iron.data.MovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Singleton
    @Provides
    static MovieRepository provideRepository(@ApplicationContext Context context) {
        return new MovieRepository(context);
    }
}