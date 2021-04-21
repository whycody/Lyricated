package com.whycody.wordslife.data.movie

class MovieRepository(private val movieDao: MovieDao, private val episodeDao: EpisodeDao) {

    fun getMovieWithId(movieId: String) = movieDao.getMovieWithId(movieId)

    fun getEpisodeWithLyricId(lyricId: Int) = episodeDao.getEpisodeWithLyricId(lyricId)
}