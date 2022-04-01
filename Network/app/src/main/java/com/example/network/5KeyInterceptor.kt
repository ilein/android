package com.example.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }

    val keyInterceptor = Interceptor { chain ->
        val url: HttpUrl = chain.request().url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
        val request = chain.request().newBuilder().url(url).build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder().apply {
        addInterceptor(keyInterceptor)
    }.build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(client)
        .build()

    val api: Api = retrofit.create(Api::class.java)

    runBlocking {
        print("MOVIE OBJECT --> ${api.getMovie(3)}")
    }
}

interface Api {
    @GET("{apiVersion}/movie/550")
    fun getMovie(@Header("apiVersion") apiVersion: Int): Call<SerialMovie>
}