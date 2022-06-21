package com.whycody.lyricated.data.app.configuration

import com.whycody.lyricated.data.AppConfiguration
import com.whycody.lyricated.data.SharedPreferenceStringLiveData

interface AppConfigurationDao {

    fun getAppConfiguration(): AppConfiguration

    fun getAppConfigurationLiveData(): SharedPreferenceStringLiveData

    fun setAppConfiguration(appConfiguration: AppConfiguration)
}