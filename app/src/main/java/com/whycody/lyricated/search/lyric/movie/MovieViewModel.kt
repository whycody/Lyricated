package com.whycody.lyricated.search.lyric.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.*
import com.whycody.lyricated.data.utilities.MovieItemMapper

class MovieViewModel(private val movieItemMapper: MovieItemMapper): ViewModel() {

    private val movieItem = MutableLiveData<MovieItem>()

    fun getMovieItem() = movieItem

    fun findMovie(extendedLyricItem: ExtendedLyricItem) =
        movieItem.postValue(movieItemMapper.getMovieItemFromExtendedLyricItem(extendedLyricItem))
}