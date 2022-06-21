package com.whycody.lyricated.search.filter.choose.source

import com.whycody.lyricated.data.MovieListItem

interface MovieItemInteractor {

    fun movieListItemClicked(movieListItem: MovieListItem)
}