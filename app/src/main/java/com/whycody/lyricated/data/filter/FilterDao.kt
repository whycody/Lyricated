package com.whycody.lyricated.data.filter

import com.whycody.lyricated.data.SortItem
import com.whycody.lyricated.data.SortOption

interface FilterDao {

    fun getFilterItems(): List<SortItem>

    fun getFilterItemWithId(id: String): SortOption
}