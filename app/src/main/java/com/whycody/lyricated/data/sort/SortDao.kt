package com.whycody.lyricated.data.sort

import com.whycody.lyricated.data.SortItem
import com.whycody.lyricated.data.SortOption

interface SortDao {

    fun getSortItems(): List<SortItem>

    fun getSortOptionWithId(id: String): SortOption
}