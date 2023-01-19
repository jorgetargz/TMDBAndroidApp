package org.jorgetargz.movies.framework.details_tv_show

import org.jorgetargz.movies.domain.models.TVShow

interface DetailsTVShowContract {

    sealed class DetailsTVShowEvent {
        class LoadTVShow(val id: Int) : DetailsTVShowEvent()
    }

    data class DetailsTVShowState(
        val tvShow: TVShow = TVShow(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}