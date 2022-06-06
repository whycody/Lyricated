package com.whycody.wordslife.data.movie

import androidx.room.*
import com.whycody.wordslife.data.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE movie_id=:movieId")
    fun getMovieWithId(movieId: String): Movie

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAllMovies()
}