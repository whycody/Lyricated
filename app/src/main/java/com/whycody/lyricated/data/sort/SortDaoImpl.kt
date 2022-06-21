package com.whycody.lyricated.data.sort

import android.content.Context
import com.whycody.lyricated.R
import com.whycody.lyricated.data.SortItem
import com.whycody.lyricated.data.SortOption
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao

class SortDaoImpl(private val context: Context,
                  private val searchConfigurationDao: SearchConfigurationDao): SortDao {

    override fun getSortItems(): List<SortItem> {
        val sortItems = mutableListOf<SortItem>()
        sortItems.add(SortItem(
            MATCH,
            context.getString(R.string.match),
            listOf(SortOption(BEST_MATCH, context.getString(R.string.best)))))
        sortItems.add(SortItem(
            LENGTH,
            context.getString(R.string.length_of_phrase),
            listOf(SortOption(LONGEST, context.getString(R.string.from_the_longest)),
                SortOption(SHORTEST, context.getString(R.string.from_the_shortest)))))
        checkCurrentSortingOption(sortItems)
        return sortItems
    }

    override fun getSortOptionWithId(id: String) =
        getSortItemWithSortOptionId(id).options.find { it.id == id }!!

    private fun getSortItemWithSortOptionId(id: String) =
        getSortItems().find { it -> it.options.map { it.id }.contains(id) }!!

    private fun checkCurrentSortingOption(sortItems: MutableList<SortItem>) {
        val currentSortingId =  searchConfigurationDao.getSearchConfiguration().sortOptionId
        sortItems.forEach { sortItem -> sortItem.options.forEach {
            if(it.id == currentSortingId) it.isChecked = true }
        }
    }

    companion object {
        const val MATCH = "match"
        const val BEST_MATCH = "best_match"
        const val LENGTH = "length"
        const val SHORTEST = "shortest"
        const val LONGEST = "longest"
    }
}