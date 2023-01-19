package org.jorgetargz.movies.domain.use_cases.tv_shows

import kotlinx.coroutines.flow.Flow
import org.jorgetargz.movies.data.TVShowsRepository
import org.jorgetargz.movies.domain.models.TVShow
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

class LoadCachedTVShowByIdUseCase @Inject constructor(
    private val repository: TVShowsRepository
) {
    operator fun invoke(id: Int): Flow<NetworkResult<TVShow>> {
        return repository.fetchTVShowCached(id)
    }
}