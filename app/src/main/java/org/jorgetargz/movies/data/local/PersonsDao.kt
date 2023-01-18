package org.jorgetargz.movies.data.local

import androidx.room.*
import org.jorgetargz.movies.data.models.entitys.KnownForEntity
import org.jorgetargz.movies.data.models.entitys.PersonEntity
import org.jorgetargz.movies.data.models.relations.PersonWithKnownFor

@Dao
interface PersonsDao {

    @Transaction
    @Query("SELECT * FROM persons order by popularity DESC")
    fun getAll(): List<PersonWithKnownFor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(persons: List<PersonEntity>, knowFor: List<KnownForEntity>)

    @Query("DELETE FROM persons")
    fun deleteAll()

    @Query("DELETE FROM known_for")
    fun deleteAllKnowFor()
}