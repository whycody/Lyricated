package com.whycody.wordslife.search.filter.choose.source.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.BR
import com.whycody.wordslife.R
import com.whycody.wordslife.data.MovieListItem
import com.whycody.wordslife.search.filter.choose.source.MovieItemInteractor

class MovieListItemAdapter(private val interactor: MovieItemInteractor): ListAdapter<MovieListItem,
        MovieListItemAdapter.MovieListItemHolder>(MovieListItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_movie, parent, false)
        return MovieListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieListItemHolder, position: Int) =
        holder.setupData(getItem(position))

    inner class MovieListItemHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(movieListItem: MovieListItem) {
            binding.setVariable(BR.position, adapterPosition)
            binding.setVariable(BR.interactor, interactor)
            binding.setVariable(BR.movieListItem, movieListItem)
            binding.executePendingBindings()
        }
    }
}