package com.whycody.lyricated.search.sort.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.BR
import com.whycody.lyricated.R
import com.whycody.lyricated.data.SortItem
import com.whycody.lyricated.search.sort.recycler.option.recycler.SortOptionAdapter

class SortItemAdapter(private val interactor: SortItemInteractor): ListAdapter<SortItem,
        SortItemAdapter.SortItemHolder>(SortItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_sort, parent, false)
        return SortItemHolder(binding)
    }

    override fun onBindViewHolder(holder: SortItemHolder, position: Int) = holder.setupData(getItem(position))

    inner class SortItemHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        private val recycler = binding.root.findViewById<RecyclerView>(R.id.sortOptionsRecycler)
        private val headerLayout = binding.root.findViewById<ConstraintLayout>(R.id.headerLayout)

        fun setupData(sortItem: SortItem) {
            headerLayout.setOnClickListener{ headerClicked(sortItem) }
            val sortOptionAdapter = SortOptionAdapter(interactor)
            sortOptionAdapter.submitList(sortItem.options)
            recycler.adapter = sortOptionAdapter
            binding.setVariable(BR.header, sortItem.headerName)
            binding.setVariable(BR.itemIsBtn, sortItem.options.isEmpty())
            binding.executePendingBindings()
        }

        private fun headerClicked(sortItem: SortItem) {
            if(sortItem.options.isEmpty())
                interactor.sortOptionClicked(sortItem.id)
        }
    }
}