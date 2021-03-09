package com.whycody.wordslife.choose.language.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.wordslife.data.Language

class ChooseLanguageDiffCallback: DiffUtil.ItemCallback<Language>() {

    override fun areItemsTheSame(oldItem: Language, newItem: Language)
        = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Language, newItem: Language)
        = oldItem == newItem
}