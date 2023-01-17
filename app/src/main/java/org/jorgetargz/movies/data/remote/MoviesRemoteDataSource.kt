package org.jorgetargz.movies.data.remote

import org.jorgetargz.movies.data.models.entitys.MovieEntity
import org.jorgetargz.movies.data.models.responses.TrendingMovieResponse
import org.jorgetargz.movies.network.services.MovieService
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

/**
 * Fetches data using the Movie API Service
 */
class MoviesRemoteDataSource @Inject constructor(private val movieService: MovieService) :
    BaseApiResponse() {

    suspend fun fetchTrendingMovies(): NetworkResult<TrendingMovieResponse> {
        return safeApiCall(apiCall = { movieService.getPopularMovies() })
    }

    suspend fun fetchMovie(id: Int): NetworkResult<MovieEntity> {
        return safeApiCall(apiCall = { movieService.getMovie(id) })
    }

}