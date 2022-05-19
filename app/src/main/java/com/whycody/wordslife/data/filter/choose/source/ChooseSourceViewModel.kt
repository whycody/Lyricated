package com.whycody.wordslife.data.filter.choose.source

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.wordslife.data.Movie
import com.whycody.wordslife.data.MovieApi
import com.whycody.wordslife.data.MovieListItem
import com.whycody.wordslife.data.api.ApiService
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.movie.MovieDao
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import kotlinx.coroutines.launch

class ChooseSourceViewModel(movieDao: MovieDao, private val searchConfDao: SearchConfigurationDao):
    ViewModel(), MovieItemInteractor {

    private var currentSearchText: String? = null
    private val allMovies = MutableLiveData(movieDao.getAllMovies())
    private val movieListItems = MutableLiveData(getMovieListItemsFromMovieItems())

    init {
        viewModelScope.launch {
            searchTextChanged(currentSearchText)
        }
    }

    fun getMovieListItems() = movieListItems

    fun searchTextChanged(newText: String?) {
        currentSearchText = newText
        if(!newText.isNullOrEmpty())
            movieListItems.postValue(getMovieListItemsFromMovieItems().filter {
                it.allTitles.contains(newText.lowercase())
            })
        else movieListItems.postValue(getMovieListItemsFromMovieItems())
    }

    private fun getMovieListItemsFromMovieItems(): List<MovieListItem> {
        val currentSearchConf = searchConfDao.getSearchConfiguration()
        return allMovies.value?.map { MovieListItem(
            it.id,
            it.eng!!,
            getAllTitlesFromMovie(it),
            currentSearchConf.chosenSource == it.id)
        }!!.sortedBy { it.title }
    }

    private fun getAllTitlesFromMovie(movie: Movie): String {
        var allTitles = with(movie) { "$eng $esp $fr $ger $it $pl $pt" }
        allTitles = allTitles.replace("null", "").lowercase()
        return allTitles
    }

    override fun movieListItemClicked(movieListItem: MovieListItem) {
        val currentSearchConf = searchConfDao.getSearchConfiguration()
        currentSearchConf.chosenSource = movieListItem.id
        currentSearchConf.checkedFilters =
            currentSearchConf.checkedFilters
                .minus(listOf(FilterDaoImpl.ONLY_MOVIES,
                    FilterDaoImpl.ONLY_SERIES, FilterDaoImpl.CHOOSE_SOURCE))
                .plus(FilterDaoImpl.CHOOSE_SOURCE)
        searchConfDao.setSearchConfiguration(currentSearchConf)
    }
}