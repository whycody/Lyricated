package com.whycody.wordslife.data.sort

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class SortDaoImpl(private val context: Context,
                  private val searchConfigurationDao: SearchConfigurationDao): SortDao {

    override fun getSortItems(): List<SortItem> {
        val sortItems = mutableListOf<SortItem>()
        sortItems.add(SortItem(context.getString(R.string.match),
            listOf(SortOption(BEST_MATCH, context.getString(R.string.best)))))
        sortItems.add(SortItem(context.getString(R.string.length_of_phrase),
            listOf(SortOption(LONGEST, context.getString(R.string.from_the_longest)),
                SortOption(SHORTEST, context.getString(R.string.from_the_shortest)))))
        checkCurrentSortingOption(sortItems)
        return sortItems
    }

    private fun checkCurrentSortingOption(sortItems: MutableList<SortItem>) {
        val currentSortingId =  searchConfigurationDao.getSearchConfiguration().sortOptionId
        sortItems.forEach { it.options.forEach { if(it.id == currentSortingId) it.isChecked = true } }
    }

    companion object {
        const val BEST_MATCH = "best_match"
        const val SHORTEST = "shortest"
        const val LONGEST = "longest"
    }
}