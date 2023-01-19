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

fun Person.toDataEntity() : PersonEntity {
    return PersonEntity(
        id = id,
        name = name,
        profile_path = profilePath,
        biography = biography,
        popularity = popularity,
        known_for_department = knownForDepartment,
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
            poster_path = it.posterPath,
            vote_average = it.voteAverage,
            vote_count = it.voteCount,
            release_date = it.releaseDate?.toString(),
            first_air_date = it.firstAirDate?.toString(),
            media_type = it.mediaType,
            overview = it.overview,
        ) }
    )
}
