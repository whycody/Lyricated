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
                    SortOption(DEFAULT, context.getString(R.string.default_setting)),
                    SortOption(DARK, context.getString(R.string.dark_mode)),
                    SortOption(LIGHT, context.getString(R.string.light_mode))
                )))
        settingsItems.add(
            SortItem(
                EDU_MATERIALS,
                context.getString(R.string.edu_materials),
                listOf(
                    SortOption(SHOW_EDU, context.getString(R.string.show)),
                    SortOption(DO_NOT_SHOW_EDU, context.getString(R.string.do_not_show))
                )))
        settingsItems.add(
            SortItem(
                HISTORY,
                context.getString(R.string.history),
                listOf(
                    SortOption(SAVE_HISTORY, context.getString(R.string.save)),
                    SortOption(DO_NOT_SAVE_HISTORY, context.getString(R.string.do_not_save))
                )))
        settingsItems.add(
            SortItem(
                DELETE_HISTORY,
                context.getString(R.string.delete_history),
                emptyList()
            ))
        settingsItems.add(
            SortItem(
                DELETE_SAVED,
                context.getString(R.string.deselect_all),
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
        const val DO_NOT_SHOW_EDU = "do not show edu"
        const val HISTORY = "history"
        const val SAVE_HISTORY = "save history"
        const val DO_NOT_SAVE_HISTORY = "do not save history"
        const val DELETE_HISTORY = "delete history"
        const val DELETE_SAVED = "delete saved"
    }
}