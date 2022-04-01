package com.example.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

fun main() {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: SerializableApi = retrofit.create(SerializableApi::class.java)

    val response: Call<JsonMovie> = api.getMovie()

    response.enqueue(object : Callback<JsonMovie> {
        override fun onResponse(call: Call<JsonMovie>, response: Response<JsonMovie>) {
            print("MOVIE OBJECT --> ${response.body().toString()}")
        }

        override fun onFailure(call: Call<JsonMovie>, t: Throwable) {
            print("ERROR --> $t")
        }
    })
}

interface SerializableApi {
    @GET("3/movie/550?api_key=${BuildConfig.API_KEY}")
    fun getMovie(): Call<JsonMovie>
}

data class JsonMovie(
    val id: Int,
    val title: String? = null,
    val posterPath: String? = null,
    val overview: String? = null,
    val releaseDate: String? = null,
    val originalTitle: String? = null,
    val popularity: Double? = null
)