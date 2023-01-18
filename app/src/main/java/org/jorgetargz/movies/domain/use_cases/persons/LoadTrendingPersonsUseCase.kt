package org.jorgetargz.movies.domain.use_cases.persons

import kotlinx.coroutines.flow.Flow
import org.jorgetargz.movies.data.PersonsRepository
import org.jorgetargz.movies.domain.models.Person
import org.jorgetargz.movies.utils.NetworkResult
import javax.inject.Inject

class LoadTrendingPersonsUseCase @Inject constructor(
    private val repository: PersonsRepository
) {
    operator fun invoke(): Flow<NetworkResult<List<Person>>> {
        return repository.fetchTrendingPersons()
    }
}