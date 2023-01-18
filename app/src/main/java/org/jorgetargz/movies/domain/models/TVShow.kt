package org.jorgetargz.movies.domain.models

import java.time.LocalDate

data class TVShow (
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = null,
    val firstAirDate: LocalDate? = null,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    )