package com.whycody.lyricated.data.app.configuration

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.whycody.lyricated.data.AppConfiguration
import com.whycody.lyricated.data.SharedPreferenceStringLiveData

class AppConfigurationDaoImpl(context: Context): AppConfigurationDao {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    private val prefsEditor = sharedPrefs.edit()
    private val gson = Gson()

    override fun getAppConfiguration(): AppConfiguration {
        val json = sharedPrefs.getString(APP_CONFIGURATION, gson.toJson(AppConfiguration()))
        return gson.fromJson(json, AppConfiguration::class.java)
    }

    override fun getAppConfigurationLiveData() = SharedPreferenceStringLiveData(sharedPrefs,
        APP_CONFIGURATION, gson.toJson(AppConfiguration()))

    override fun setAppConfiguration(appConfiguration: AppConfiguration) {
        prefsEditor.putString(APP_CONFIGURATION, gson.toJson(appConfiguration))
        prefsEditor.commit()
    }

    companion object {
        const val APP_CONFIGURATION = "app configuration"
    }
}