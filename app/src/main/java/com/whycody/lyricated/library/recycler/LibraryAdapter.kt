package com.whycody.lyricated.library.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.data.LibraryItem
import com.whycody.lyricated.BR
import com.whycody.lyricated.R

class LibraryAdapter(private val libraryInteractor: LibraryInteractor):
    ListAdapter<LibraryItem, RecyclerView.ViewHolder>(LibraryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_library,
            parent, false)
        return LibraryHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as LibraryHolder).setupData(getItem(position))

    inner class LibraryHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun setupData(libraryItem: LibraryItem) {
            binding.setVariable(BR.libraryItem, libraryItem)
            binding.setVariable(BR.libraryInteractor, libraryInteractor)
            binding.setVariable(BR.position, layoutPosition)
            binding.executePendingBindings()
        }
    }
}