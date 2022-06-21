package com.whycody.lyricated.search.sort.recycler.option.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.BR
import com.whycody.lyricated.R
import com.whycody.lyricated.data.SortOption
import com.whycody.lyricated.search.sort.recycler.SortItemInteractor

class SortOptionAdapter(private val interactor: SortItemInteractor): ListAdapter<SortOption,
        SortOptionAdapter.SortOptionHolder>(SortOptionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortOptionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_sort_option, parent, false)
        return SortOptionHolder(binding)
    }

    override fun onBindViewHolder(holder: SortOptionHolder, position: Int) {
        holder.setupData(getItem(position))
    }

    inner class SortOptionHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(sortOption: SortOption) {
            binding.setVariable(BR.interactor, interactor)
            binding.setVariable(BR.sortOption, sortOption)
            binding.executePendingBindings()
        }
    }
}