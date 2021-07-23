package com.whycody.wordslife.search.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.filter.FilterDao
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.search.sort.recycler.SortItemInteractor

class FilterViewModel(private val filterDao: FilterDao,
                      private val searchConfigurationDao: SearchConfigurationDao): ViewModel(), SortItemInteractor {

    private val filterItems = MutableLiveData(filterDao.getFilterItems())

    fun getFilterItems() = filterItems

    override fun sortOptionClicked(sortOptionId: String) {
        val searchConf = searchConfigurationDao.getSearchConfiguration()
        val currentFilters = searchConf.checkedFilters.toMutableList()
        if(!currentFilters.contains(sortOptionId)) {
            uncheckAllOptionsFromFilter(sortOptionId, currentFilters)
            currentFilters.add(sortOptionId)
        } else currentFilters.remove(sortOptionId)
        searchConf.checkedFilters = currentFilters
        searchConfigurationDao.setSearchConfiguration(searchConf)
        filterItems.postValue(filterDao.getFilterItems())
    }

    private fun uncheckAllOptionsFromFilter(sortOptionId: String, currentFilters: MutableList<String>) {
        val filterItems = filterDao.getFilterItems()
        filterItems.find { it.options.map { it.id }
            .contains(sortOptionId) }?.options?.forEach { currentFilters.remove(it.id) }
    }
}