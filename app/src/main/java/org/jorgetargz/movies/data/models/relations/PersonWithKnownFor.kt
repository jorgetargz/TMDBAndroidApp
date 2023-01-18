package org.jorgetargz.movies.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.jorgetargz.movies.data.models.entitys.KnownForEntity
import org.jorgetargz.movies.data.models.entitys.PersonEntity
import org.jorgetargz.movies.data.models.entitys.toDomain
import org.jorgetargz.movies.domain.models.Person

data class PersonWithKnownFor(
    @Embedded
    val person: PersonEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "person_id",
    )
    val knowFor: List<KnownForEntity>,
)

fun PersonWithKnownFor.toDomain() : Person {
    return Person(
        id = person.id,
        name = person.name,
        profilePath = person.profile_path,
        popularity = person.popularity,
        knownForDepartment = person.known_for_department,
        knownFor = knowFor.map { it.toDomain() }
    )
}

fun Person.toDataRelation() : PersonWithKnownFor {
    return PersonWithKnownFor(
        person = PersonEntity(
            id = id,
            name = name,
            profile_path = profilePath,
            popularity = popularity,
            known_for_department = knownForDepartment,
        ),
        knowFor = knownFor.map { KnownForEntity(
            id = it.id,
            person_id = id,
            title = it.title,
            name = it.name,
            poster_path = it.poster_path,
            vote_average = it.vote_average,
            vote_count = it.vote_count,
            release_date = it.release_date,
            first_air_date = it.first_air_date,
            overview = it.overview,
        ) }
    )
}
