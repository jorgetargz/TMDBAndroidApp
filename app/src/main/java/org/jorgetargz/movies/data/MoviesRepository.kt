package org.jorgetargz.movies.data


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jorgetargz.movies.data.local.MoviesDao
import org.jorgetargz.movies.data.models.entitys.toMovie
import org.jorgetargz.movies.data.models.entitys.toMovieEntity
import org.jorgetargz.movies.data.remote.MoviesRemoteDataSource
import org.jorgetargz.movies.domain.models.Movie
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

/**
 * Repository which fetches Movies from Remote or Local data sources
 */
class MoviesRepository @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val moviesDao: MoviesDao
) {

    fun fetchTrendingMovies(): Flow<NetworkResult<List<Movie>>> {
        return flow {
            emit(trendingMoviesCached())
            emit(NetworkResult.Loading())
            val result = moviesRemoteDataSource.fetchTrendingMovies()
                .map { response -> response?.results?.map { it.toMovie() } ?: emptyList() }

            //Cache to database if response is successful
            if (result is NetworkResult.Success) {
                result.data?.let { it ->
                    moviesDao.deleteAll()
                    moviesDao.insertAll(it.map { it.toMovieEntity() })
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun fetchTrendingMoviesCached(): Flow<NetworkResult<List<Movie>>> =
        flow {
            emit(trendingMoviesCached())
        }.flowOn(Dispatchers.IO)

    private fun trendingMoviesCached(): NetworkResult<List<Movie>> =
        moviesDao.getAll().let { list ->
            NetworkResult.Success(list.map { it.toMovie() })
        }

    fun fetchMovie(id: Int): Flow<NetworkResult<Movie>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(moviesRemoteDataSource.fetchMovie(id).map { it?.toMovie() ?: Movie() })
        }.flowOn(Dispatchers.IO)
    }
}