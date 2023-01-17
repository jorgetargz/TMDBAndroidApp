package org.jorgetargz.movies.network.services

import org.jorgetargz.movies.data.models.entitys.TVShowEntity
import org.jorgetargz.movies.data.models.responses.TrendingTVShowResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API Services for TV Shows
 */
interface TVShowsService {

    @GET("/3/trending/tv/week")
    suspend fun getPopularTVShows(): Response<TrendingTVShowResponse>

    @GET("/3/tv/{tv_id}")
    suspend fun getTVShow(@Path("tv_id") id: Int): Response<TVShowEntity>
}