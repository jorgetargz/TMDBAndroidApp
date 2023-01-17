package org.jorgetargz.movies.framework.list_trending_tv_shows

import org.jorgetargz.movies.domain.models.TVShow

interface ListTrendingTVShowsContract {

    sealed class ListTrendingTVShowsEvent {
        class FilterTrendingTVShows(val nombre: String) : ListTrendingTVShowsEvent()
        object LoadTrendingTVShows: ListTrendingTVShowsEvent()
    }

    data class ListTrendingTVShowsState(
        val tvShows: List<TVShow> = emptyList(),
        val tvShowsFiltered: List<TVShow> = emptyList(),
        val isLoading : Boolean = false,
        val error: String? = null,
    )
}