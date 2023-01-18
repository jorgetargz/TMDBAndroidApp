package org.jorgetargz.movies.data.models.responses

import org.jorgetargz.movies.domain.models.Person

data class PersonResponse(
    val id: Int = 0,
    val name: String = "",
    val profile_path: String? = null,
    val popularity: Double = 0.0,
    val known_for_department: String = "",
    val known_for: List<KnownForResponse> = emptyList(),
)

fun PersonResponse.toDomain(): Person {
    return Person(
        id = id,
        name = name,
        profilePath = profile_path,
        popularity = popularity,
        knownForDepartment = known_for_department,
        knownFor = known_for.map { it.toDomain() }
    )
}