package com.whycody.wordslife.data.filter.choose.source

import com.whycody.wordslife.data.MovieListItem

interface MovieItemInteractor {

    fun movieListItemClicked(movieListItem: MovieListItem)
}