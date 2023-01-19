package org.jorgetargz.movies.framework.details_movie

import org.jorgetargz.movies.domain.models.Movie

interface DetailsMovieContract {

    sealed class DetailsMovieEvent {
        class LoadMovie(val id: Int): DetailsMovieEvent()
    }

    data class DetailsMovieState(
        val movie: Movie = Movie(),
        val isLoading : Boolean = false,
        val error: String? = null,
    )
}