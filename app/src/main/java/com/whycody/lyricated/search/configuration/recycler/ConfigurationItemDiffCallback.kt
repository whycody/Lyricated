package com.whycody.lyricated.search.configuration.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.lyricated.data.ConfigurationItem

class ConfigurationItemDiffCallback: DiffUtil.ItemCallback<ConfigurationItem>() {

    override fun areItemsTheSame(oldItem: ConfigurationItem, newItem: ConfigurationItem)
        = oldItem.confId == newItem.confId

    override fun areContentsTheSame(oldItem: ConfigurationItem, newItem: ConfigurationItem)
        = oldItem == newItem
}