package com.whycody.lyricated.search.sort

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.data.sort.SortDao
import com.whycody.lyricated.search.sort.recycler.SortItemInteractor

class SortViewModel(sortDao: SortDao, private val searchConfigurationDao: SearchConfigurationDao):
    ViewModel(), SortItemInteractor {

    private val sortItems = MutableLiveData(sortDao.getSortItems())

    fun getSortItems() = sortItems

    override fun sortOptionClicked(sortOptionId: String) =
        searchConfigurationDao.setSortingOption(sortOptionId)
}