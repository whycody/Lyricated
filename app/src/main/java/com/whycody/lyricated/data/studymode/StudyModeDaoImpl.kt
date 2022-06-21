package com.whycody.lyricated.data.studymode

import android.content.Context
import com.whycody.lyricated.R
import com.whycody.lyricated.data.SortItem
import com.whycody.lyricated.data.SortOption
import com.whycody.lyricated.data.app.configuration.AppConfigurationDao

class StudyModeDaoImpl(private val context: Context,
                       private val appConfigurationDao: AppConfigurationDao): StudyModeDao {

    override fun getStudyModeSettingsItems(): List<SortItem> {
        val studyModeSettingsItems = mutableListOf<SortItem>()
        studyModeSettingsItems.add(
            SortItem(
                DIFFICULTY,
                context.getString(R.string.difficulty),
                listOf(
                    SortOption(EASY, context.getString(R.string.easy)),
                    SortOption(MEDIUM, context.getString(R.string.medium)),
                    SortOption(HARD, context.getString(R.string.hard))
                )))
        checkCurrentSettings(studyModeSettingsItems)
        return studyModeSettingsItems
    }

    private fun checkCurrentSettings(studyModeSettingsItems: List<SortItem>) {
        val appConfig = appConfigurationDao.getAppConfiguration()
        studyModeSettingsItems.find { it.id == DIFFICULTY }?.options?.find {
            it.id == appConfig.studyModeDifficulty }?.isChecked = true
    }

    companion object {
        const val DIFFICULTY = "difficulty"
        const val EASY = "easy"
        const val MEDIUM = "medium"
        const val HARD = "hard"
    }
}