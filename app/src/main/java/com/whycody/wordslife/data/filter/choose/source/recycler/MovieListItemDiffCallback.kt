package com.whycody.wordslife.data.filter.choose.source.recycler

import androidx.recyclerview.widget.DiffUtil
import com.whycody.wordslife.data.MovieListItem

class MovieListItemDiffCallback: DiffUtil.ItemCallback<MovieListItem>() {

    override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem)
        = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem)
        = oldItem == newItem
}