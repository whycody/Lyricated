package com.whycody.wordslife.data.movie

import androidx.room.Dao
import androidx.room.Query
import com.whycody.wordslife.data.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE movie_id=:movieId")
    fun getMovieWithId(movieId: String): Movie

    @Query("SELECT * FROM movies ORDER BY RANDOM() LIMIT 1")
    fun getRandomMovie(): Movie

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>
}