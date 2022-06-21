package com.whycody.lyricated.search.sort.recycler.option.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.lyricated.data.SortOption

class SortOptionDiffCallback: DiffUtil.ItemCallback<SortOption>() {

    override fun areItemsTheSame(oldItem: SortOption, newItem: SortOption)
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SortOption, newItem: SortOption)
            = oldItem == newItem
}