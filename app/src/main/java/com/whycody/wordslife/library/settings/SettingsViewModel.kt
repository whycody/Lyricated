package com.whycody.wordslife.library.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.app.configuration.AppConfigurationDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.settings.SettingsDao
import com.whycody.wordslife.data.settings.SettingsDaoImpl
import com.whycody.wordslife.search.sort.recycler.SortItemInteractor

class SettingsViewModel(private val settingsDao: SettingsDao,
                        private val searchConfigurationDao: SearchConfigurationDao,
                        private val appConfigurationDao: AppConfigurationDao,
                        private val lastSearchRepo: LastSearchRepository):
    ViewModel(), SortItemInteractor {

    private val settingsItems = MutableLiveData(settingsDao.getSettingsItems())

    fun getSettingsItems() = settingsItems

    fun refreshSettingsItems() = settingsItems.postValue(settingsDao.getSettingsItems())

    override fun sortOptionClicked(sortOptionId: String) {
        val sortItem = getSortItemFromSortOptionId(sortOptionId)
        when (sortItem?.id) {
            SettingsDaoImpl.EDU_MATERIALS -> updateEduMaterials(sortOptionId)
            SettingsDaoImpl.DELETE_HISTORY -> deleteHistory()
            SettingsDaoImpl.DELETE_SAVED -> deleteSaved()
            else -> updateAppConfig(sortOptionId, sortItem)
        }
        refreshSettingsItems()
    }

    private fun deleteHistory() = lastSearchRepo.deleteAllLastSearches()

    private fun deleteSaved() = lastSearchRepo.deselectAllLastSearches()

    private fun updateEduMaterials(sortOptionId: String) {
        val currentSearchConf = searchConfigurationDao.getSearchConfiguration()
        currentSearchConf.educationalMaterials = sortOptionId
        searchConfigurationDao.setSearchConfiguration(currentSearchConf)
    }

    private fun updateAppConfig(sortOptionId: String, sortItem: SortItem?) {
        val appConfiguration = appConfigurationDao.getAppConfiguration()
        if(sortItem?.id == SettingsDaoImpl.APPEARANCE) appConfiguration.appearance = sortOptionId
        else appConfiguration.history = sortOptionId
        appConfigurationDao.setAppConfiguration(appConfiguration)
    }

    private fun getSortItemFromSortOptionId(sortOptionId: String) =
        settingsItems.value!!.find { it.id == sortOptionId } ?:
        settingsItems.value!!.find { it.options.map { it.id }.contains(sortOptionId) }
}