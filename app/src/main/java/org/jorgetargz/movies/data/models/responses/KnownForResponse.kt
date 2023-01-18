package org.jorgetargz.movies.data.models.responses

import org.jorgetargz.movies.domain.models.KnownFor
import java.time.LocalDate

class KnownForResponse(
    val id: Int = 0,
    val title: String? = null,
    val name: String? = null,
    val poster_path: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val release_date: String?,
    val first_air_date: String?,
    val overview: String = "",
)

fun KnownForResponse.toDomain(): KnownFor {
    return KnownFor(
        id = id,
        title = title,
        name = name,
        posterPath = poster_path,
        voteAverage = vote_average,
        voteCount = vote_count,
        releaseDate = release_date?.let { LocalDate.parse(it) },
        firstAirDate = first_air_date?.let { LocalDate.parse(it) },
        overview = overview,
    )
}
