package com.whycody.wordslife.search.sort.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.BR
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SortItem
import com.whycody.wordslife.search.sort.recycler.option.recycler.SortOptionAdapter

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
        private val headerArrow = binding.root.findViewById<ImageView>(R.id.headerArrow)
        private val headerLayout = binding.root.findViewById<ConstraintLayout>(R.id.headerLayout)

        fun setupData(sortItem: SortItem) {
            if(!sortItem.expanded) headerClicked()
            headerLayout.setOnClickListener{ headerClicked() }
            val sortOptionAdapter = SortOptionAdapter(interactor)
            sortOptionAdapter.submitList(sortItem.options)
            recycler.adapter = sortOptionAdapter
            binding.setVariable(BR.header, sortItem.headerName)
            binding.executePendingBindings()
        }

        private fun headerClicked() {
            val itemIsCollapsed = recycler.visibility == View.GONE
            headerArrow.rotation = if(itemIsCollapsed) 180f else 0f
            headerArrow.animate()?.rotationBy(180f)?.setDuration(200)?.start()
            recycler.visibility = if(itemIsCollapsed) View.VISIBLE else View.GONE
        }
    }
}