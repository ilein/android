package com.pg.homeworknetwork

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*

class Api {
    private val baseUrl = "api.themoviedb.org"
    private val movies = "${BuildConfig.API_VERSION}/movie/popular"
    private val movie = "${BuildConfig.API_VERSION}/movie/"

    val ktorLogger = object : Logger {
        override fun log(message: String) {
            if (message.contains("api_key=")) {
                println("${message.split("=").first()}=BuildConfig.API_KEY")
            } else {
                println(message)
            }
        }
    }

    private val ktorClient = HttpClient() {
        install(Logging) {
            logger = ktorLogger
            level = LogLevel.BODY
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getMovie(movieId: Int): Movie = ktorClient.use {
        it.get(
            host = baseUrl,
            path = movie + movieId.toString()
        ) {
            parameter("api_key", BuildConfig.API_KEY)
        }
    }

    suspend fun getMovies(): Movies = ktorClient.use {
        it.get(
            host = baseUrl,
            path = movies
        ) {
            parameter("api_key", BuildConfig.API_KEY)
        }
    }
}