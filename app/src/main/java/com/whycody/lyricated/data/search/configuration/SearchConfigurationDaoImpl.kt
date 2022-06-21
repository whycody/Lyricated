package com.whycody.lyricated.data.search.configuration

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.whycody.lyricated.data.LyricLanguages
import com.whycody.lyricated.data.SharedPreferenceStringLiveData
import com.whycody.lyricated.data.SearchConfiguration
import com.whycody.lyricated.data.filter.FilterDaoImpl

class SearchConfigurationDaoImpl(context: Context): SearchConfigurationDao {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    private val prefsEditor = sharedPrefs.edit()
    private val gson = Gson()

    override fun getSearchConfiguration(): SearchConfiguration {
        val json = sharedPrefs.getString(SEARCH_CONFIGURATION, gson.toJson(SearchConfiguration()))
        return gson.fromJson(json, SearchConfiguration::class.java)
    }

    override fun getSearchConfigurationLiveData() =
        SharedPreferenceStringLiveData(sharedPrefs,
            SEARCH_CONFIGURATION, gson.toJson(SearchConfiguration()))

    override fun setSearchConfiguration(searchConfiguration: SearchConfiguration) {
        removeChosenSource(searchConfiguration)
        prefsEditor.putString(SEARCH_CONFIGURATION, gson.toJson(searchConfiguration))
        prefsEditor.commit()
    }

    private fun removeChosenSource(searchConf: SearchConfiguration) {
        if(!searchConf.checkedFilters.contains(FilterDaoImpl.CHOOSE_SOURCE))
            searchConf.chosenSource = null
    }

    override fun setSortingOption(sortOptionId: String) {
        val searchConfig = getSearchConfiguration()
        searchConfig.sortOptionId = sortOptionId
        setSearchConfiguration(searchConfig)
    }

    override fun getLyricLanguages(): LyricLanguages = getSearchConfiguration().lyricLanguages

    companion object {
        const val SEARCH_CONFIGURATION = "search_configuration"
        const val SORT_TYPE = "sort type"
        const val FILTER_TYPE = "filter type"
    }
}