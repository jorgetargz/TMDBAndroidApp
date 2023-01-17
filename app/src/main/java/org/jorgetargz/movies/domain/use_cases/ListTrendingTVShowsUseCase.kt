package org.jorgetargz.movies.domain.use_cases

import kotlinx.coroutines.flow.Flow
import org.jorgetargz.movies.data.TVShowsRepository
import org.jorgetargz.movies.domain.models.TVShow
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

class ListTrendingTVShowsUseCase @Inject constructor(
    private val repository: TVShowsRepository
) {
    operator fun invoke(): Flow<NetworkResult<List<TVShow>>> {
        return repository.fetchTrendingTVShows()
    }
}