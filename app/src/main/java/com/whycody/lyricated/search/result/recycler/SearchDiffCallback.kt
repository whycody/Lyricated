package com.whycody.lyricated.search.result.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.lyricated.data.LyricItem

class SearchDiffCallback: DiffUtil.ItemCallback<LyricItem>() {

    override fun areItemsTheSame(oldItem: LyricItem, newItem: LyricItem)
        = oldItem.lyricId == newItem.lyricId

    override fun areContentsTheSame(oldItem: LyricItem, newItem: LyricItem)
        = oldItem == newItem
}