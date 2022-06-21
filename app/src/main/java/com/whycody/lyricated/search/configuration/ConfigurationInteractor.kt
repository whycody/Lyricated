package com.whycody.lyricated.search.configuration

import com.whycody.lyricated.data.ConfigurationItem

interface ConfigurationInteractor {

    fun confRemoved(confItem: ConfigurationItem)
}