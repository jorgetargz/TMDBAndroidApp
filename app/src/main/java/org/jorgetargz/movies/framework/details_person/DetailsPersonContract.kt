package org.jorgetargz.movies.framework.details_person

import org.jorgetargz.movies.domain.models.Person

interface DetailsPersonContract {

    sealed class DetailsPersonEvent {
        class LoadPerson(val id: Int) : DetailsPersonEvent()
    }

    data class DetailsPersonState(
        val person: Person = Person(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}