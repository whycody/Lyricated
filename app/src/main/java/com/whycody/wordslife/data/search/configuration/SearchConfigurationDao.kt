package com.whycody.wordslife.data.search.configuration

import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SearchConfiguration
import com.whycody.wordslife.data.SharedPreferenceStringLiveData

interface SearchConfigurationDao {

    fun getSearchConfiguration(): SearchConfiguration

    fun getSearchConfigurationLiveData(): SharedPreferenceStringLiveData

    fun setSearchConfiguration(searchConfiguration: SearchConfiguration)

    fun setSortingOption(sortOptionId: String)

    fun getLyricLanguages(): LyricLanguages
}