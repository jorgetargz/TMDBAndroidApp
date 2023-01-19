package org.jorgetargz.movies.domain.models

data class Person(
    val id: Int = 0,
    val name: String = "",
    val profilePath: String? = null,
    val biography: String? = null,
    val popularity: Double = 0.0,
    val knownForDepartment: String = "",
    val knownFor: List<KnownFor> = emptyList(),
)
