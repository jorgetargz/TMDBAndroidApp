package org.jorgetargz.movies.domain.use_cases.movies

import kotlinx.coroutines.flow.Flow
import org.jorgetargz.movies.data.MoviesRepository
import org.jorgetargz.movies.domain.models.Movie
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

class LoadCachedTrendingMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    operator fun invoke(): Flow<NetworkResult<List<Movie>>> {
        return repository.fetchTrendingMoviesCached()
    }
}