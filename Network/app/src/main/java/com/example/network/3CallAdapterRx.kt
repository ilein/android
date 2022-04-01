package com.example.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.http.GET

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val api: RxApi = retrofit.create(RxApi::class.java)

    lateinit var response: SerialMovie

    api.getMovie()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { movie ->
            response = movie
            print("MOVIE OBJECT --> $response")
        }
}

interface RxApi {
    @GET("3/movie/550?api_key=${BuildConfig.API_KEY}")
    fun getMovie(): Single<SerialMovie>
}