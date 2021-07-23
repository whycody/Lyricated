package com.whycody.wordslife.data.filter

import com.whycody.wordslife.data.SortItem

interface FilterDao {

    fun getFilterItems(): List<SortItem>
}