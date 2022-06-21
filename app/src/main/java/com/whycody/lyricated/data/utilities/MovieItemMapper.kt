package com.whycody.lyricated.data.utilities

import com.whycody.lyricated.data.ExtendedLyricItem
import com.whycody.lyricated.data.Movie
import com.whycody.lyricated.data.MovieItem
import com.whycody.lyricated.data.MovieListItem

interface MovieItemMapper {

    fun getMovieListItemFromMovie(movie: Movie): MovieListItem

    fun getMovieItemFromExtendedLyricItem(extendedLyricItem: ExtendedLyricItem): MovieItem
}