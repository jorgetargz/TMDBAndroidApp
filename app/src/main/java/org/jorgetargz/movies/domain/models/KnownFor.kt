package org.jorgetargz.movies.domain.models

import java.time.LocalDate

data class KnownFor(
    val id: Int = 0,
    val title: String? = null,
    val name: String? = null,
    val posterPath: String? = null,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val releaseDate: LocalDate?,
    val firstAirDate: LocalDate?,
    val mediaType : String? = null,
    val overview: String = "",
)
