package com.whycody.wordslife.search.lyric.vocabulary.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.wordslife.data.VocabularyItem

class VocabularyDiffCallback: DiffUtil.ItemCallback<VocabularyItem>() {

    override fun areItemsTheSame(oldItem: VocabularyItem, newItem: VocabularyItem) =
        oldItem.index == newItem.index

    override fun areContentsTheSame(oldItem: VocabularyItem, newItem: VocabularyItem) =
        oldItem == newItem
}