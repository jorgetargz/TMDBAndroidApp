package org.jorgetargz.movies.data.models.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jorgetargz.movies.domain.models.KnownFor
import java.time.LocalDate

@Entity(tableName = "known_for", foreignKeys = [
    ForeignKey(
        entity = PersonEntity::class,
        parentColumns = ["id"],
        childColumns = ["person_id"],
    )
])
data class KnownForEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var person_id: Int = 0,
    val title: String? = null,
    val name: String? = null,
    val poster_path: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val release_date: String?,
    val first_air_date: String?,
    val media_type: String? = null,
    val overview: String = "",
)

fun KnownForEntity.toDomain(): KnownFor {
    return KnownFor(
        id = id,
        title = title,
        name = name,
        posterPath = poster_path,
        voteAverage = vote_average,
        voteCount = vote_count,
        releaseDate = release_date?.let { LocalDate.parse(it) },
        firstAirDate = first_air_date?.let { LocalDate.parse(it) },
        mediaType = media_type,
        overview = overview,
    )
}
