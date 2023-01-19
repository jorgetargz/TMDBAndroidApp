package org.jorgetargz.movies.framework.list_trending_persons

interface ListTrendingPersonsActions {

    fun onPersonClicked(id: Int)
    fun onTVShowClicked(id: Int)
    fun onMovieClicked(id: Int)
}