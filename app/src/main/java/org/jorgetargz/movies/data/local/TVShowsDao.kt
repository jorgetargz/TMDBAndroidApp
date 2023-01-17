package org.jorgetargz.movies.data.local

import androidx.room.*
import org.jorgetargz.movies.data.models.entitys.TVShowEntity

@Dao
interface TVShowsDao {

    @Query("SELECT * FROM shows order by popularity DESC")
    fun getAll(): List<TVShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<TVShowEntity>)

    @Query("DELETE FROM shows")
    fun deleteAll()
}