package com.example.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val api = KtorApi()

    runBlocking {
        print("MOVIE OBJECT --> ${api.getMovie()}")
    }
}

class KtorApi {
    private val base = "api.themoviedb.org"
    private val movie = "3/movie/550"

    //TODO добавить в предыдущий пример
    val kLogger = object : Logger {
        override fun log(message: String) {
            if (message.contains("api_key=")) {
                println("${message.split("=").first()}=BuildConfig.API_KEY")
            } else {
                println(message)
            }
        }
    }

    private val ktorClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = kLogger
            level = LogLevel.BODY
        }
    }

    suspend fun getMovie(): SerialMovie = ktorClient.get(
        host = base,
        path = movie
    ) {
        parameter("api_key", BuildConfig.API_KEY)
    }
}