package com.whycody.wordslife.data.api

import com.whycody.wordslife.data.FindLyricsBody
import com.whycody.wordslife.data.FindLyricsResponse
import com.whycody.wordslife.data.MoviesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val BASE_URL = "https://translate.lyricated.com/"

interface ApiService {

    @GET("get_movies")
    suspend fun getAllMovies(): Response<MoviesResponse>

    @POST("find_lyrics")
    suspend fun findLyrics(@Body findLyricsBody: FindLyricsBody): Response<FindLyricsResponse>
}