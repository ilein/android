package com.example.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val api: KotlinSerializableApi = retrofit.create(KotlinSerializableApi::class.java)

    val response: Call<SerialMovie> = api.getMovie()

    response.enqueue(object : Callback<SerialMovie> {
        override fun onResponse(call: Call<SerialMovie>, response: Response<SerialMovie>) {
            print("MOVIE OBJECT --> ${response.body().toString()}")
        }

        override fun onFailure(call: Call<SerialMovie>, t: Throwable) {
            print("ERROR --> $t")
        }
    })
}

interface KotlinSerializableApi {
    @GET("3/movie/550?api_key=${BuildConfig.API_KEY}")
    fun getMovie(): Call<SerialMovie>
}

@Serializable
data class SerialMovie(
    @SerialName("id") //TODO рассказать
    val id: Int,
    @SerialName("title")
    val title: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null
)