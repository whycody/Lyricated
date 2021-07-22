package com.whycody.wordslife.search.sort.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.wordslife.data.SortItem

class SortItemDiffCallback: DiffUtil.ItemCallback<SortItem>() {

    override fun areItemsTheSame(oldItem: SortItem, newItem: SortItem)
        = oldItem.headerName == newItem.headerName

    override fun areContentsTheSame(oldItem: SortItem, newItem: SortItem)
        = oldItem == newItem
}