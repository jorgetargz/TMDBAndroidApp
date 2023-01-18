package org.jorgetargz.movies.framework.list_trending_persons

import org.jorgetargz.movies.domain.models.Person

interface ListTrendingPersonsContract {

    sealed class ListTrendingPersonsEvent {
        class FilterTrendingPersons(val nombre: String) : ListTrendingPersonsEvent()
        object LoadTrendingPersons: ListTrendingPersonsEvent()
    }

    data class ListTrendingPersonsState(
        val persons: List<Person> = emptyList(),
        val personsFiltered: List<Person> = emptyList(),
        val isLoading : Boolean = false,
        val error: String? = null,
    )
}