package com.whycody.wordslife.search.lyric.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.utilities.MovieItemMapper

class MovieViewModel(private val movieItemMapper: MovieItemMapper): ViewModel() {

    private val movieItem = MutableLiveData<MovieItem>()

    fun getMovieItem() = movieItem

    fun findMovie(extendedLyricItem: ExtendedLyricItem) =
        movieItem.postValue(movieItemMapper.getMovieItemFromExtendedLyricItem(extendedLyricItem))
}