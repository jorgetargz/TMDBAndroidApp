package org.jorgetargz.movies.network.services

import org.jorgetargz.movies.data.models.entitys.PersonEntity
import org.jorgetargz.movies.data.models.responses.TrendingPersonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API Services for Persons
 */
interface PersonsService {

    @GET("/3/trending/person/week")
    suspend fun getPopularPersons(): Response<TrendingPersonResponse>

    @GET("/3/person/{person_id}")
    suspend fun getPerson(@Path("person_id") id: Int): Response<PersonEntity>
}