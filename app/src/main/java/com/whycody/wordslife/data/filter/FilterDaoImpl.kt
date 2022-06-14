package com.whycody.wordslife.data.filter

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.Movie
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption
import com.whycody.wordslife.data.movie.MovieRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class FilterDaoImpl(private val context: Context,
                    private val searchConfigurationDao: SearchConfigurationDao,
                    private val movieRepository: MovieRepository): FilterDao {

    override fun getFilterItems(): List<SortItem> {
        val filterItems = mutableListOf<SortItem>()
        filterItems.add(
            SortItem(TYPE,
                context.getString(R.string.source),
                listOf(SortOption(ONLY_MOVIES, context.getString(R.string.only_movies)),
                    SortOption(ONLY_SERIES, context.getString(R.string.only_series)),
                    SortOption(CHOOSE_SOURCE, getMovieSortOptionName()))))
        filterItems.add(
            SortItem(CURSES,
                context.getString(R.string.curses),
                listOf(SortOption(WITHOUT_CURSES, context.getString(R.string.without_curses)))))
        checkCurrentFilterOptions(filterItems)
        return filterItems
    }

    private fun getMovieSortOptionName(): String {
        val currentSearchConf = searchConfigurationDao.getSearchConfiguration()
        if(!currentSearchConf.checkedFilters.contains(CHOOSE_SOURCE))
            return context.getString(R.string.choose_source)
        val movie = movieRepository.getMovieWithId(currentSearchConf.chosenSource!!)
        return "${movie.eng!!} (${getTypeOfMovie(movie)})"
    }

    private fun getTypeOfMovie(movie: Movie): String {
        return if(movie.type == "serie") context.getString(R.string.series)
        else context.getString(R.string.movie)
    }

    override fun getFilterItemWithId(id: String) =
        getFilterItemWithFilterOptionId(id).options.find { it.id == id }!!

    private fun getFilterItemWithFilterOptionId(id: String) =
        getFilterItems().find { filterItem -> filterItem.options.map { it.id }.contains(id) }!!

    private fun checkCurrentFilterOptions(filterItems: MutableList<SortItem>) {
        val currentCheckedFilters = searchConfigurationDao.getSearchConfiguration().checkedFilters
        filterItems.forEach { filterItem -> filterItem.options.forEach {
            if(currentCheckedFilters.contains(it.id)) it.isChecked = true } }
    }

    companion object {
        const val TYPE = "type"
        const val ONLY_MOVIES = "only_movies"
        const val ONLY_SERIES = "only_series"
        const val CHOOSE_SOURCE = "choose_source"
        const val CURSES = "curses"
        const val WITHOUT_CURSES = "without_curses"
        val DEFAULT_FILTERS = emptyList<String>()
    }
}