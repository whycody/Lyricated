package com.whycody.wordslife.data.app.configuration

import com.whycody.wordslife.data.AppConfiguration
import com.whycody.wordslife.data.SharedPreferenceStringLiveData

interface AppConfigurationDao {

    fun getAppConfiguration(): AppConfiguration

    fun getAppConfigurationLiveData(): SharedPreferenceStringLiveData

    fun setAppConfiguration(appConfiguration: AppConfiguration)
}