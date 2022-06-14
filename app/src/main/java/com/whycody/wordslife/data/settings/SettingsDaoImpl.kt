package com.whycody.wordslife.data.settings

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption
import com.whycody.wordslife.data.app.configuration.AppConfigurationDao
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class SettingsDaoImpl(private val context: Context,
                      private val appConfigurationDao: AppConfigurationDao,
                      private val searchConfigurationDao: SearchConfigurationDao): SettingsDao {

    override fun getSettingsItems(): List<SortItem> {
        val settingsItems = mutableListOf<SortItem>()
        settingsItems.add(
            SortItem(
                APPEARANCE,
                context.getString(R.string.appearance),
                listOf(
                    SortOption(DEFAULT, context.getString(R.string.defaultsetting)),
                    SortOption(DARK, context.getString(R.string.darkmode)),
                    SortOption(LIGHT, context.getString(R.string.lightmode))
                )))
        settingsItems.add(
            SortItem(
                EDU_MATERIALS,
                context.getString(R.string.edumaterials),
                listOf(
                    SortOption(SHOW_EDU, context.getString(R.string.show)),
                    SortOption(DONT_SHOW_EDU, context.getString(R.string.dontshow))
                )))
        settingsItems.add(
            SortItem(
                HISTORY,
                context.getString(R.string.history),
                listOf(
                    SortOption(SAVE_HISTORY, context.getString(R.string.save)),
                    SortOption(DONT_SAVE_HISTORY, context.getString(R.string.dontsave))
                )))
        settingsItems.add(
            SortItem(
                DELETE_HISTORY,
                context.getString(R.string.deletehistory),
                emptyList()
            ))
        settingsItems.add(
            SortItem(
                DELETE_SAVED,
                context.getString(R.string.deselectall),
                emptyList()
            ))
        checkCurrentSettings(settingsItems)
        return settingsItems
    }

    private fun checkCurrentSettings(settingsItems: MutableList<SortItem>) {
        val searchConfig = searchConfigurationDao.getSearchConfiguration()
        val appConfig = appConfigurationDao.getAppConfiguration()
        settingsItems.forEach{ settingItem -> settingItem.options.forEach{ it.isChecked = false }}
        settingsItems.find { it.id == APPEARANCE }?.options?.find {
            it.id == appConfig.appearance}?.isChecked = true
        settingsItems.find { it.id == EDU_MATERIALS }?.options?.find {
            it.id == searchConfig.educationalMaterials}?.isChecked = true
        settingsItems.find { it.id == HISTORY }?.options?.find {
            it.id == appConfig.history}?.isChecked = true
    }

    companion object {
        const val APPEARANCE = "appearance"
        const val DEFAULT = "default"
        const val DARK = "dark"
        const val LIGHT = "light"
        const val EDU_MATERIALS = "edu materials"
        const val SHOW_EDU = "show edu"
        const val DONT_SHOW_EDU = "dont show edu"
        const val HISTORY = "history"
        const val SAVE_HISTORY = "save history"
        const val DONT_SAVE_HISTORY = "dont save history"
        const val DELETE_HISTORY = "delete history"
        const val DELETE_SAVED = "delete saved"
    }
}