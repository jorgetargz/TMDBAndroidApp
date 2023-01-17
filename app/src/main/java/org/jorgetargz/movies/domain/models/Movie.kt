package org.jorgetargz.movies.domain.models

data class Movie (
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = null,
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    )