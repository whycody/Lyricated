package com.whycody.lyricated.home.history

import androidx.recyclerview.widget.DiffUtil
import com.whycody.lyricated.data.HistoryItem

class HistoryDiffCallback: DiffUtil.ItemCallback<HistoryItem>() {

    override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem)
        = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem)
        = oldItem == newItem
}