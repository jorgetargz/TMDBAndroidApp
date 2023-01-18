package org.jorgetargz.movies.data.models.responses

import org.jorgetargz.movies.domain.models.KnownFor

class KnownForResponse(
    val id: Int = 0,
    val title: String? = null,
    val name: String? = null,
    val poster_path: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val release_date: String? = null,
    val first_air_date: String? = null,
    val overview: String = "",
)

fun KnownForResponse.toDomain(): KnownFor {
    return KnownFor(
        id = id,
        title = title,
        name = name,
        poster_path = poster_path,
        vote_average = vote_average,
        vote_count = vote_count,
        release_date = release_date,
        first_air_date = first_air_date,
        overview = overview,
    )
}
