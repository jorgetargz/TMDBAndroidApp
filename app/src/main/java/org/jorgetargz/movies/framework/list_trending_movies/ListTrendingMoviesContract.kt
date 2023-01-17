package org.jorgetargz.movies.framework.list_trending_movies

import org.jorgetargz.movies.domain.models.Movie

interface ListTrendingMoviesContract {

    sealed class ListTrendingMoviesEvent {
        class FilterTrendingMoviesMovies(val nombre: String) : ListTrendingMoviesEvent()
        object LoadTrendingMoviesMovies: ListTrendingMoviesEvent()
    }

    data class ListTrendingMoviesState(
        val movies: List<Movie> = emptyList(),
        val moviesFiltered: List<Movie> = emptyList(),
        val isLoading : Boolean = false,
        val error: String? = null,
    )
}