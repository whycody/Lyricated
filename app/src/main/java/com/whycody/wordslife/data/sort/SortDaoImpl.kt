package com.whycody.wordslife.data.sort

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption

class SortDaoImpl(private val context: Context): SortDao {

    override fun getSortItems(): List<SortItem> {
        val sortItems = mutableListOf<SortItem>()
        sortItems.add(SortItem(context.getString(R.string.match),
            listOf(SortOption(BEST_MATCH, context.getString(R.string.best)))))
        sortItems.add(SortItem(context.getString(R.string.length_of_phrase),
            listOf(SortOption(LONGEST, context.getString(R.string.from_the_longest)),
                SortOption(SHORTEST, context.getString(R.string.from_the_shortest)))))
        return sortItems
    }

    companion object {
        const val BEST_MATCH = "best_match"
        const val SHORTEST = "shortest"
        const val LONGEST = "longest"
    }
}