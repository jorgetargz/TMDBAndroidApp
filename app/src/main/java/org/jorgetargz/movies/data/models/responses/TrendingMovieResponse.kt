package org.jorgetargz.movies.data.models.responses

import org.jorgetargz.movies.data.models.entitys.MovieEntity

class TrendingMovieResponse(
    val results: List<MovieEntity>?
)