package org.jorgetargz.movies.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.jorgetargz.movies.data.local.AppDatabase
import org.jorgetargz.movies.data.local.MoviesDao
import org.jorgetargz.movies.data.local.PersonsDao
import org.jorgetargz.movies.data.local.TVShowsDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMoviesDao(appDatabase: AppDatabase): MoviesDao {
        return appDatabase.moviesDao()
    }

    @Provides
    fun provideTVShowsDao(appDatabase: AppDatabase): TVShowsDao {
        return appDatabase.tvShowsDao()
    }

    @Provides
    fun providePersonsDao(appDatabase: AppDatabase): PersonsDao {
        return appDatabase.personsDao()
    }
}