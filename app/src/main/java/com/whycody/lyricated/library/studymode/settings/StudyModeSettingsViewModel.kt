package com.whycody.lyricated.library.studymode.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.app.configuration.AppConfigurationDao
import com.whycody.lyricated.data.studymode.StudyModeDao
import com.whycody.lyricated.data.studymode.StudyModeDaoImpl
import com.whycody.lyricated.search.sort.recycler.SortItemInteractor

class StudyModeSettingsViewModel(private val studyModeDao: StudyModeDao,
    private val appConfigurationDao: AppConfigurationDao): ViewModel(), SortItemInteractor {

    private val studyModeSettingsItems = MutableLiveData(studyModeDao.getStudyModeSettingsItems())

    fun getStudyModeSettingsItems() = studyModeSettingsItems

    private fun refreshStudyModeSettingsItems() =
        studyModeSettingsItems.postValue(studyModeDao.getStudyModeSettingsItems())

    override fun sortOptionClicked(sortOptionId: String) {
        val currentAppConfig = appConfigurationDao.getAppConfiguration()
        val sortItem = getSortItemFromSortOptionId(sortOptionId)
        if(sortItem?.id == StudyModeDaoImpl.DIFFICULTY)
            currentAppConfig.studyModeDifficulty = sortOptionId
        appConfigurationDao.setAppConfiguration(currentAppConfig)
        refreshStudyModeSettingsItems()
    }

    private fun getSortItemFromSortOptionId(sortOptionId: String) =
        studyModeSettingsItems.value!!.find { settingItem -> settingItem.options
            .map { it.id }.contains(sortOptionId) }
}