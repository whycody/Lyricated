package com.whycody.wordslife.library.studymode.vocabulary

import androidx.recyclerview.widget.DiffUtil
import com.whycody.wordslife.data.ExtendedVocabularyItem

class StudyModeVocabularyDiffCallback: DiffUtil.ItemCallback<ExtendedVocabularyItem>() {

    override fun areItemsTheSame(oldItem: ExtendedVocabularyItem, newItem: ExtendedVocabularyItem) =
        oldItem.index == newItem.index

    override fun areContentsTheSame(oldItem: ExtendedVocabularyItem, newItem: ExtendedVocabularyItem) =
        oldItem == newItem
}