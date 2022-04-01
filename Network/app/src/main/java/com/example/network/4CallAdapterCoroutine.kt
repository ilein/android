package com.example.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
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

    val api: CoroutineApi = retrofit.create(CoroutineApi::class.java)

    runBlocking {
        print("MOVIE OBJECT --> ${api.getMovie()}")
    }
}

interface CoroutineApi {
    @GET("3/movie/550?api_key=${BuildConfig.API_KEY}")
    suspend fun getMovie(): SerialMovie
}