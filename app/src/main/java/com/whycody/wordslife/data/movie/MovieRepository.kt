package com.whycody.wordslife.data.movie

import com.whycody.wordslife.data.Movie

class MovieRepository(private val movieDao: MovieDao) {

    fun getMovieWithId(movieId: String) = movieDao.getMovieWithId(movieId)

    fun getAllMovies() = movieDao.getAllMovies()

    fun insertMovies(movies: List<Movie>) = movieDao.insertMovies(movies)

    fun deleteAllMovies() = movieDao.deleteAllMovies()
}