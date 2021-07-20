package com.whycody.wordslife.data.sort

import com.whycody.wordslife.data.SortItem

interface SortDao {

    fun getSortItems(): List<SortItem>
}