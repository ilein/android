package com.pg.homeworknetwork

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<Movie> = ArrayList()
)

@Serializable
data class Movie (
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null

)