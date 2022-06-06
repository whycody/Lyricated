package com.whycody.wordslife.search.filter.choose.source

import com.whycody.wordslife.data.MovieListItem

interface MovieItemInteractor {

    fun movieListItemClicked(movieListItem: MovieListItem)
}