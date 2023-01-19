package org.jorgetargz.movies.data


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jorgetargz.movies.data.local.TVShowsDao
import org.jorgetargz.movies.data.models.entitys.toDataEntity
import org.jorgetargz.movies.data.models.entitys.toDomain
import org.jorgetargz.movies.data.remote.TVShowsRemoteDataSource
import org.jorgetargz.movies.domain.models.TVShow
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

/**
 * Repository which fetches TV Shows from Remote or Local data sources
 */
class TVShowsRepository @Inject constructor(
    private val tvShowsRemoteDataSource: TVShowsRemoteDataSource,
    private val tvShowsDao: TVShowsDao,
) {

    fun fetchTrendingTVShows(): Flow<NetworkResult<List<TVShow>>> {
        return flow {
            emit(trendingTVShowsCached())
            emit(NetworkResult.Loading())
            val result = tvShowsRemoteDataSource.fetchTrendingTVShows()
                .map { response -> response?.results
                    ?.map { it.toDomain() }
                    ?.sortedWith(compareByDescending { it.popularity })
                    ?: emptyList() }

            //Cache to database if response is successful
            if (result is NetworkResult.Success) {
                result.data?.let { it ->
                    tvShowsDao.deleteAll()
                    tvShowsDao.insertAll(it.map { it.toDataEntity() })
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun fetchTrendingTVShowsCached(): Flow<NetworkResult<List<TVShow>>> =
        flow {
            emit(trendingTVShowsCached())
        }.flowOn(Dispatchers.IO)

    private fun trendingTVShowsCached(): NetworkResult<List<TVShow>> =
        tvShowsDao.getAll().let { list ->
            NetworkResult.Success(list.map { it.toDomain() })
        }

    fun fetchTVShow(id: Int): Flow<NetworkResult<TVShow>> {
        return flow {
            emit(tvShowCachedById(id))
            emit(NetworkResult.Loading())

            val result = tvShowsRemoteDataSource.fetchTVShow(id)
                .map { response -> response?.toDomain() ?: TVShow() }

            //Cache to database if response is successful
            if (result is NetworkResult.Success) {
                result.data?.let { it ->
                    tvShowsDao.update(it.toDataEntity())
                }
            }
            emit(tvShowsRemoteDataSource.fetchTVShow(id).map { it?.toDomain() ?: TVShow() })
        }.flowOn(Dispatchers.IO)
    }

    fun fetchTVShowCached(id: Int): Flow<NetworkResult<TVShow>> {
        return flow {
            emit(tvShowCachedById(id))
        }.flowOn(Dispatchers.IO)
    }

    private fun tvShowCachedById(id: Int): NetworkResult<TVShow> =
        tvShowsDao.getById(id)?.let { tvShow ->
            NetworkResult.Success(tvShow.toDomain())
        } ?: NetworkResult.Success(TVShow())
}