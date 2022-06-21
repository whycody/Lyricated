package com.whycody.lyricated.search.configuration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.ConfigurationItem
import com.whycody.lyricated.data.filter.FilterDao
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDaoImpl
import com.whycody.lyricated.data.sort.SortDao
import com.whycody.lyricated.data.sort.SortDaoImpl

class ConfigurationViewModel(private val searchConfigurationDao: SearchConfigurationDao,
                             private val filterDao: FilterDao, private val sortDao: SortDao):
    ViewModel(), ConfigurationInteractor {

    private val confItems = MutableLiveData(getConfigurationItems())

    fun getConfItems() = confItems

    fun searchConfUpdated() = confItems.postValue(getConfigurationItems())

    override fun confRemoved(confItem: ConfigurationItem) {
        val searchConf = searchConfigurationDao.getSearchConfiguration()
        if(confItem.type == SearchConfigurationDaoImpl.SORT_TYPE)
            searchConf.sortOptionId = SortDaoImpl.BEST_MATCH
        else searchConf.checkedFilters = searchConf.checkedFilters.minus(confItem.confId)
        searchConfigurationDao.setSearchConfiguration(searchConf)
        confItems.postValue(getConfigurationItems())
    }

    private fun getConfigurationItems(): List<ConfigurationItem> {
        val searchConf = searchConfigurationDao.getSearchConfiguration()
        val confItems = mutableListOf<ConfigurationItem>()
        val sortOptionId = searchConf.sortOptionId
        if(sortOptionId != SortDaoImpl.BEST_MATCH) addSortOption(confItems, sortOptionId)
        if(searchConf.checkedFilters.isNotEmpty()) addFilterOptions(confItems, searchConf.checkedFilters)
        searchConfigurationDao.setSearchConfiguration(searchConf)
        return confItems
    }

    private fun addSortOption(confItems: MutableList<ConfigurationItem>, sortOptionId: String) =
        confItems.add(ConfigurationItem(sortDao.getSortOptionWithId(sortOptionId).name.lowercase(),
            sortOptionId, SearchConfigurationDaoImpl.SORT_TYPE))

    private fun addFilterOptions(confItems: MutableList<ConfigurationItem>, filters: List<String>) =
        filters.forEach { addFilterOption(confItems, it) }

    private fun addFilterOption(confItems: MutableList<ConfigurationItem>, filterId: String) =
        confItems.add(ConfigurationItem(filterDao.getFilterItemWithId(filterId).name.lowercase(),
            filterId, SearchConfigurationDaoImpl.FILTER_TYPE))
}