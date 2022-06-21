package com.whycody.lyricated.data.studymode

import com.whycody.lyricated.data.SortItem

interface StudyModeDao {

    fun getStudyModeSettingsItems(): List<SortItem>
}