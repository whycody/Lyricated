package com.whycody.wordslife.data.studymode

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.data.SortOption
import com.whycody.wordslife.data.app.configuration.AppConfigurationDao

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
        studyModeSettingsItems.add(
            SortItem(
                SOURCE,
                context.getString(R.string.source),
                listOf(
                    SortOption(RANDOM, context.getString(R.string.random)),
                    SortOption(HISTORY, context.getString(R.string.history)),
                    SortOption(SAVED, context.getString(R.string.saved))
                )))
        checkCurrentSettings(studyModeSettingsItems)
        return studyModeSettingsItems
    }

    private fun checkCurrentSettings(studyModeSettingsItems: List<SortItem>) {
        val appConfig = appConfigurationDao.getAppConfiguration()
        studyModeSettingsItems.find { it.id == DIFFICULTY }?.options?.find {
            it.id == appConfig.studyModeDifficulty }?.isChecked = true
        studyModeSettingsItems.find { it.id == SOURCE }?.options?.find {
            it.id == appConfig.studyModeSource }?.isChecked = true
    }

    companion object {
        const val DIFFICULTY = "difficulty"
        const val EASY = "easy"
        const val MEDIUM = "medium"
        const val HARD = "hard"
        const val SOURCE = "source"
        const val RANDOM = "random"
        const val HISTORY = "history"
        const val SAVED = "saved"
    }
}