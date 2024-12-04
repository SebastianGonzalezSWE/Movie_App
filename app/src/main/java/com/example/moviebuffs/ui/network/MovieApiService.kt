package com.example.moviebuffs.ui.network

import retrofit2.Retrofit
import retrofit2.http.GET
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType

private const val BASE_URL =
    "https://kareemy.github.io"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface MovieAPIService {
    @GET("MovieBuffs/movies.json")
    suspend fun getMovies(): List<Movie>
}

object MovieApi{
    val retrofitService : MovieAPIService by lazy {
        retrofit.create(MovieAPIService::class.java)
    }
}