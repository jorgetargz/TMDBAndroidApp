package org.jorgetargz.movies.data


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jorgetargz.movies.data.local.PersonsDao
import org.jorgetargz.movies.data.models.entitys.toDomain
import org.jorgetargz.movies.data.models.relations.toDataRelation
import org.jorgetargz.movies.data.models.relations.toDomain
import org.jorgetargz.movies.data.models.responses.toDomain
import org.jorgetargz.movies.data.remote.PersonsRemoteDataSource
import org.jorgetargz.movies.domain.models.Person
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

/**
 * Repository which fetches Persons from Remote or Local data sources
 */
class PersonsRepository @Inject constructor(
    private val personsRemoteDataSource: PersonsRemoteDataSource,
    private val personsDao: PersonsDao
) {

    fun fetchTrendingPersons(): Flow<NetworkResult<List<Person>>> {
        return flow {
            emit(trendingPersonsCached())
            emit(NetworkResult.Loading())
            val result = personsRemoteDataSource.fetchTrendingPersons()
                .map { response -> response?.results
                    ?.map { it.toDomain() }
                    ?.sortedWith(compareByDescending { it.popularity })
                    ?: emptyList()
                }

            //Cache to database if response is successful
            if (result is NetworkResult.Success) {
                result.data?.let { it ->
                    personsDao.deleteAllKnowFor()
                    personsDao.deleteAll()
                    val persons = it.map { it.toDataRelation() }
                    persons.forEach { person ->
                        person.knowFor.forEach { knowFor ->
                            knowFor.person_id = person.person.id
                        }
                    }
                    personsDao.insertAll(
                        persons.map { personWithKnownFor -> personWithKnownFor.person },
                        persons.flatMap { personWithKnownFor -> personWithKnownFor.knowFor }
                    )
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun fetchTrendingPersonsCached(): Flow<NetworkResult<List<Person>>> =
        flow {
            emit(trendingPersonsCached())
        }.flowOn(Dispatchers.IO)

    private fun trendingPersonsCached(): NetworkResult<List<Person>> =
        personsDao.getAll().let { list ->
            NetworkResult.Success(list.map { it.toDomain() })
        }

    fun fetchPerson(id: Int): Flow<NetworkResult<Person>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(personsRemoteDataSource.fetchPerson(id).map { it?.toDomain() ?: Person() })
        }.flowOn(Dispatchers.IO)
    }
}