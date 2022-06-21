package com.whycody.lyricated.data.settings

import com.whycody.lyricated.data.SortItem

interface SettingsDao {

    fun getSettingsItems(): List<SortItem>
}