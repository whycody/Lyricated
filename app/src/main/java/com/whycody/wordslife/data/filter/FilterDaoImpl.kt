package com.whycody.wordslife.data.filter

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class FilterDaoImpl(private val context: Context,
                    private val searchConfigurationDao: SearchConfigurationDao): FilterDao {

    override fun getFilterItems(): List<SortItem> {
        val filterItems = mutableListOf<SortItem>()
        filterItems.add(
            SortItem(TYPE,
                context.getString(R.string.type),
                listOf(SortOption(ONLY_MOVIES, context.getString(R.string.only_movies)),
                    SortOption(ONLY_SERIES, context.getString(R.string.only_series)))))
        filterItems.add(
            SortItem(CURSES,
                context.getString(R.string.curses),
                listOf(SortOption(WITHOUT_CURSES, context.getString(R.string.without_curses)))))
        checkCurrentFilterOptions(filterItems)
        return filterItems
    }

    private fun checkCurrentFilterOptions(filterItems: MutableList<SortItem>) {
        val currentCheckedFilters = searchConfigurationDao.getSearchConfiguration().checkedFilters
        filterItems.forEach { it.options.forEach { if(currentCheckedFilters.contains(it.id)) it.isChecked = true } }
    }

    companion object {
        const val TYPE = "type"
        const val ONLY_MOVIES = "only movies"
        const val ONLY_SERIES = "only series"
        const val CURSES = "curses"
        const val WITHOUT_CURSES = "without curses"
    }
}