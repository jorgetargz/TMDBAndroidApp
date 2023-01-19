package org.jorgetargz.movies.data.local

import androidx.room.*
import org.jorgetargz.movies.data.models.entitys.MovieEntity

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies order by popularity DESC")
    fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getById(id: Int): MovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    fun deleteAll()
}