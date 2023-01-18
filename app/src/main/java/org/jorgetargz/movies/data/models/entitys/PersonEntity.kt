package org.jorgetargz.movies.data.models.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jorgetargz.movies.domain.models.Person

@Entity(tableName = "persons")
data class PersonEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val profile_path: String? = null,
    val popularity: Double = 0.0,
    val known_for_department: String = "",
)

fun PersonEntity.toDomain(): Person = Person(
    id = id,
    name = name,
    profilePath = profile_path,
    popularity = popularity,
    knownForDepartment = known_for_department,
)
