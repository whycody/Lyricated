package com.whycody.wordslife.search.configuration

import com.whycody.wordslife.data.ConfigurationItem

interface ConfigurationInteractor {

    fun confRemoved(confItem: ConfigurationItem)
}