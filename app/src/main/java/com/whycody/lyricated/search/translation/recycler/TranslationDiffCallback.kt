package com.whycody.lyricated.search.translation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.lyricated.data.Translation

class TranslationDiffCallback: DiffUtil.ItemCallback<Translation>() {

    override fun areItemsTheSame(oldItem: Translation, newItem: Translation)
        = oldItem == newItem

    override fun areContentsTheSame(oldItem: Translation, newItem: Translation)
        = oldItem == newItem
}