package com.whycody.wordslife.data.search.configuration

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.SearchConfiguration

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
        prefsEditor.putString(SEARCH_CONFIGURATION, gson.toJson(searchConfiguration))
        prefsEditor.commit()
    }

    override fun getLyricLanguages(): LyricLanguages = getSearchConfiguration().lyricLanguages

    companion object {
        const val SEARCH_CONFIGURATION = "search_configuration"
    }
}