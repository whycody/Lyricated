package com.whycody.wordslife.data.utilities

import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.Movie
import com.whycody.wordslife.data.MovieItem
import com.whycody.wordslife.data.MovieListItem

interface MovieItemMapper {

    fun getMovieListItemFromMovie(movie: Movie): MovieListItem

    fun getMovieItemFromExtendedLyricItem(extendedLyricItem: ExtendedLyricItem): MovieItem
}