package org.jorgetargz.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.jorgetargz.movies.data.models.entitys.KnownForEntity
import org.jorgetargz.movies.data.models.entitys.MovieEntity
import org.jorgetargz.movies.data.models.entitys.PersonEntity
import org.jorgetargz.movies.data.models.entitys.TVShowEntity

@Database(
    entities = [MovieEntity::class, TVShowEntity::class, PersonEntity::class, KnownForEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun tvShowsDao(): TVShowsDao
    abstract fun personsDao(): PersonsDao
}
