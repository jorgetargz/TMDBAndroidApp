package org.jorgetargz.movies.network.services

import org.jorgetargz.movies.data.models.entitys.MovieEntity
import org.jorgetargz.movies.data.models.responses.TrendingMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API Services for Movies
 */
interface MoviesService {

    @GET("/3/trending/movie/week")
    suspend fun getPopularMovies(): Response<TrendingMovieResponse>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int): Response<MovieEntity>
}