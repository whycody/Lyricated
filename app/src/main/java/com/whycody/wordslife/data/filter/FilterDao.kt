package com.whycody.wordslife.data.filter

import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption

interface FilterDao {

    fun getFilterItems(): List<SortItem>

    fun getFilterItemWithId(id: String): SortOption
}