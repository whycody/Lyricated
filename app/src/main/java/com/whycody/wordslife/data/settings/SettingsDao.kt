package com.whycody.wordslife.data.settings

import com.whycody.wordslife.data.SortItem

interface SettingsDao {

    fun getSettingsItems(): List<SortItem>
}