package com.whycody.wordslife.data.api

import com.whycody.wordslife.data.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET

const val BASE_URL = "https://translate.lyricated.com/"

interface ApiService {

    @GET("get_movies")
    suspend fun getAllMovies(): Response<MoviesResponse>

}