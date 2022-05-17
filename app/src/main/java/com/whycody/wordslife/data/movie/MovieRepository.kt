package com.whycody.wordslife.data.movie

class MovieRepository(private val movieDao: MovieDao) {

    fun getMovieWithId(movieId: String) = movieDao.getMovieWithId(movieId)
}