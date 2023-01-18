package org.jorgetargz.movies.data.remote

import org.jorgetargz.movies.data.models.entitys.PersonEntity
import org.jorgetargz.movies.data.models.responses.TrendingPersonResponse
import org.jorgetargz.movies.network.services.PersonsService
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

/**
 * Fetches data using the Persons API Service
 */
class PersonsRemoteDataSource @Inject constructor(private val personsService: PersonsService) :
    BaseApiResponse() {

    suspend fun fetchTrendingPersons(): NetworkResult<TrendingPersonResponse> {
        return safeApiCall(apiCall = { personsService.getPopularPersons() })
    }

    suspend fun fetchPerson(id: Int): NetworkResult<PersonEntity> {
        return safeApiCall(apiCall = { personsService.getPerson(id) })
    }

}