package com.whycody.lyricated.library.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.SortItem
import com.whycody.lyricated.data.app.configuration.AppConfigurationDao
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.data.settings.SettingsDao
import com.whycody.lyricated.data.settings.SettingsDaoImpl
import com.whycody.lyricated.search.sort.recycler.SortItemInteractor

class SettingsViewModel(private val settingsDao: SettingsDao,
                        private val searchConfigurationDao: SearchConfigurationDao,
                        private val appConfigurationDao: AppConfigurationDao):
    ViewModel(), SortItemInteractor {

    private val settingsItems = MutableLiveData(settingsDao.getSettingsItems())
    private val actionBtnClicked = MutableLiveData<String>(null)

    fun getSettingsItems() = settingsItems

    fun getActionBtnClicked() = actionBtnClicked

    fun refreshSettingsItems() = settingsItems.postValue(settingsDao.getSettingsItems())

    override fun sortOptionClicked(sortOptionId: String) {
        val sortItem = getSortItemFromSortOptionId(sortOptionId)
        when (sortItem?.id) {
            SettingsDaoImpl.EDU_MATERIALS -> updateEduMaterials(sortOptionId)
            SettingsDaoImpl.DELETE_HISTORY, SettingsDaoImpl.DELETE_SAVED ->
                actionBtnClicked.postValue(sortItem.id)
            else -> updateAppConfig(sortOptionId, sortItem)
        }
        refreshSettingsItems()
    }

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
        settingsItems.value!!.find { settingItem ->
            settingItem.options.map { it.id }.contains(sortOptionId) }
}