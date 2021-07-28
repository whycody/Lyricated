package com.whycody.wordslife.data.filter.choose.source

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.Movie
import com.whycody.wordslife.data.MovieListItem
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.movie.MovieDao
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class ChooseSourceViewModel(private val movieDao: MovieDao,
                            private val searchConfDao: SearchConfigurationDao):
    ViewModel(), MovieItemInteractor {

    private val movieListItems = MutableLiveData(getMovieListItemsFromDao())

    fun getMovieListItems() = movieListItems

    private fun getMovieListItemsFromDao(): List<MovieListItem> {
        val currentSearchConf = searchConfDao.getSearchConfiguration()
        return movieDao.getAllMovies().map { MovieListItem(
            it.movieId,
            it.eng!!,
            getAllTitlesFromMovie(it),
            currentSearchConf.chosenSource == it.movieId)
        }.sortedBy { it.title }
    }

    private fun getAllTitlesFromMovie(movie: Movie): String {
        val allTitles = with(movie) { "$eng $esp $fr $ger $it $pl $pt" }
        allTitles.replace("null", "")
        return allTitles
    }

    override fun movieListItemClicked(movieListItem: MovieListItem) {
        val currentSearchConf = searchConfDao.getSearchConfiguration()
        currentSearchConf.chosenSource = movieListItem.id
        currentSearchConf.checkedFilters =
            currentSearchConf.checkedFilters
                .minus(listOf(FilterDaoImpl.ONLY_MOVIES, FilterDaoImpl.ONLY_SERIES, FilterDaoImpl.CHOOSE_SOURCE))
                .plus(FilterDaoImpl.CHOOSE_SOURCE)
        searchConfDao.setSearchConfiguration(currentSearchConf)
    }
}