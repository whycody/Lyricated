package com.whycody.wordslife.search.filter.choose.source

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.wordslife.data.MovieListItem
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.movie.MovieRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.utilities.MovieItemMapper
import kotlinx.coroutines.launch

class ChooseSourceViewModel(movieRepository: MovieRepository, private val movieItemMapper: MovieItemMapper,
                            private val searchConfDao: SearchConfigurationDao):
    ViewModel(), MovieItemInteractor {

    private var currentSearchText: String? = null
    private val listOfAllMovieListItems = movieRepository.getAllMovies()
        .map { movieItemMapper.getMovieListItemFromMovie(it) }.sortedBy { it.title }
    private val allMovieListItems = MutableLiveData(listOfAllMovieListItems)
    private val movieListItems = MutableLiveData(listOfAllMovieListItems)

    init {
        viewModelScope.launch {
            searchTextChanged(currentSearchText)
        }
    }

    fun getMovieListItems() = movieListItems

    fun searchTextChanged(newText: String?) {
        currentSearchText = newText
        if(!newText.isNullOrEmpty())
            movieListItems.postValue(allMovieListItems.value!!
                .filter { it.allTitles.contains(newText.lowercase()) })
        else movieListItems.postValue(allMovieListItems.value)
    }

    override fun movieListItemClicked(movieListItem: MovieListItem) {
        val currentSearchConf = searchConfDao.getSearchConfiguration()
        currentSearchConf.chosenSource = movieListItem.id
        currentSearchConf.checkedFilters =
            currentSearchConf.checkedFilters
                .minus(listOf(FilterDaoImpl.ONLY_MOVIES,
                        FilterDaoImpl.ONLY_SERIES, FilterDaoImpl.CHOOSE_SOURCE).toSet())
                .plus(FilterDaoImpl.CHOOSE_SOURCE)
        searchConfDao.setSearchConfiguration(currentSearchConf)
    }
}