package org.jorgetargz.movies.data



import org.jorgetargz.movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jorgetargz.movies.data.local.TVShowsDao
import org.jorgetargz.movies.data.models.entitys.*
import org.jorgetargz.movies.data.remote.TVShowsRemoteDataSource
import org.jorgetargz.movies.domain.models.TVShow
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
            emit(fetchTrendingTVShowsCached())
            emit(NetworkResult.Loading())
            val result = tvShowsRemoteDataSource.fetchTrendingTVShows()
                .map { response ->  response?.results?.map { it.toTVShow() } ?: emptyList()}

            //Cache to database if response is successful
            if (result is NetworkResult.Success) {
                result.data?.let { it ->
                    tvShowsDao.deleteAll()
                    tvShowsDao.insertAll(it.map{ it.toTVShowEntity()})
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    private fun fetchTrendingTVShowsCached(): NetworkResult<List<TVShow>> =
            tvShowsDao.getAll().let { list->
                NetworkResult.Success(list.map { it.toTVShow() })
            }

    fun fetchTVShow(id: Int): Flow<NetworkResult<TVShow>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(tvShowsRemoteDataSource.fetchTVShow(id).map { it?.toTVShow() ?: TVShow() })
        }.flowOn(Dispatchers.IO)
    }
}