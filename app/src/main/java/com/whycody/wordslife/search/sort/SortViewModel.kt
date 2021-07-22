package com.whycody.wordslife.search.sort

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.sort.SortDao
import com.whycody.wordslife.search.sort.recycler.SortItemInteractor

class SortViewModel(sortDao: SortDao, private val searchConfigurationDao: SearchConfigurationDao):
    ViewModel(), SortItemInteractor {

    private val sortItems = MutableLiveData(sortDao.getSortItems())

    fun getSortItems() = sortItems

    override fun sortOptionClicked(sortOptionId: String) =
        searchConfigurationDao.setSortingOption(sortOptionId)
}