package com.whycody.wordslife.data.sort

import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption

interface SortDao {

    fun getSortItems(): List<SortItem>

    fun getSortOptionWithId(id: String): SortOption
}