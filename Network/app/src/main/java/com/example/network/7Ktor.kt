package com.example.network

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val api = KtorShortApi()

    runBlocking {
        print("MOVIE OBJECT --> ${api.getMovie()}")
    }
}

class KtorShortApi {
    private val base = "api.themoviedb.org"
    private val movie = "3/movie/550?api_key=${BuildConfig.API_KEY}"

    suspend fun getMovie(): String = HttpClient().use {
        it.get(
            host = base,
            path = movie
        )
    }
}