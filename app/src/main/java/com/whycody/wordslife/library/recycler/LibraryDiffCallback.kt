package com.whycody.wordslife.library.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.wordslife.data.LibraryItem

class LibraryDiffCallback: DiffUtil.ItemCallback<LibraryItem>() {

    override fun areItemsTheSame(oldItem: LibraryItem, newItem: LibraryItem)
        = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: LibraryItem, newItem: LibraryItem)
        = oldItem == newItem
}