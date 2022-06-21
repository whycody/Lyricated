package com.whycody.lyricated.home.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.BR
import com.whycody.lyricated.R
import com.whycody.lyricated.data.HistoryItem
import com.whycody.lyricated.home.HistoryInteractor

class HistoryAdapter(private val historyInteractor: HistoryInteractor):
        ListAdapter<HistoryItem, HistoryAdapter.HistoryHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_history, parent, false)
        return HistoryHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.setupData(getItem(position))
    }

    inner class HistoryHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(historyItem: HistoryItem) {
            binding.setVariable(BR.historyItem, historyItem)
            binding.setVariable(BR.position, layoutPosition)
            binding.setVariable(BR.interactor, historyInteractor)
            binding.executePendingBindings()
        }
    }
}