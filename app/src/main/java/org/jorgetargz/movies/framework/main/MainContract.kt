package org.jorgetargz.movies.framework.main

interface MainContract {

    sealed class Event {
        object MensajeMostrado: Event()
    }

    data class State(
        val error: String? = null,
    )

}