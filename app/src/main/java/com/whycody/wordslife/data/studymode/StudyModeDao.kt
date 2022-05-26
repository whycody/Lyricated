package com.whycody.wordslife.data.studymode

import com.whycody.wordslife.data.SortItem

interface StudyModeDao {

    fun getStudyModeSettingsItems(): List<SortItem>
}