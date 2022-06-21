package com.whycody.lyricated.data.search.configuration

import com.whycody.lyricated.data.LyricLanguages
import com.whycody.lyricated.data.SearchConfiguration
import com.whycody.lyricated.data.SharedPreferenceStringLiveData

interface SearchConfigurationDao {

    fun getSearchConfiguration(): SearchConfiguration

    fun getSearchConfigurationLiveData(): SharedPreferenceStringLiveData

    fun setSearchConfiguration(searchConfiguration: SearchConfiguration)

    fun setSortingOption(sortOptionId: String)

    fun getLyricLanguages(): LyricLanguages
}