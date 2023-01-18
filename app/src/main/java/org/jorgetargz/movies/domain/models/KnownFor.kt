package org.jorgetargz.movies.domain.models

data class KnownFor(
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
