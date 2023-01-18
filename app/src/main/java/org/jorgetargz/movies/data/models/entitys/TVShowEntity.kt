package org.jorgetargz.movies.data.models.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jorgetargz.movies.domain.models.TVShow
import java.time.LocalDate

@Entity(tableName = "shows")
data class TVShowEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
)

fun TVShowEntity.toDomain(): TVShow = TVShow(
    id = id,
    name = name,
    overview = overview,
    popularity = popularity,
    posterPath = poster_path,
    firstAirDate = LocalDate.parse(first_air_date),
    voteAverage = vote_average,
    voteCount = vote_count,
)

fun TVShow.toDataEntity(): TVShowEntity = TVShowEntity(
    id = id,
    name = name,
    overview = overview,
    popularity = popularity,
    poster_path = posterPath,
    first_air_date = firstAirDate.toString(),
    vote_average = voteAverage,
    vote_count = voteCount,
)