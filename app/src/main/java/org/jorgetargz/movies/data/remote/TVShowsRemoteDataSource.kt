package org.jorgetargz.movies.data.remote

import org.jorgetargz.movies.data.models.entitys.TVShowEntity
import org.jorgetargz.movies.data.models.responses.TrendingTVShowResponse
import org.jorgetargz.movies.network.services.TVShowsService
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

/**
 * Fetches data using the TV Shows API Service
 */
class TVShowsRemoteDataSource @Inject constructor(private val tvShowsService: TVShowsService) :
    BaseApiResponse() {

    suspend fun fetchTrendingTVShows(): NetworkResult<TrendingTVShowResponse> {
        return safeApiCall(apiCall = { tvShowsService.getPopularTVShows() })
    }

    suspend fun fetchTVShow(id: Int): NetworkResult<TVShowEntity> {
        return safeApiCall(apiCall = { tvShowsService.getTVShow(id) })
    }

}