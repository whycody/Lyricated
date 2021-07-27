package com.whycody.wordslife.search.configuration.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.R
import com.whycody.wordslife.data.ConfigurationItem
import com.whycody.wordslife.search.configuration.ConfigurationInteractor

class ConfigurationItemAdapter(private val interactor: ConfigurationInteractor): ListAdapter<ConfigurationItem,
        ConfigurationItemAdapter.ConfigurationItemHolder>(ConfigurationItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigurationItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_configuration, parent, false)
        return ConfigurationItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ConfigurationItemHolder, position: Int) =
        holder.setupData(getItem(position))

    inner class ConfigurationItemHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(confItem: ConfigurationItem) {
            binding.setVariable(BR.confItem, confItem)
            binding.setVariable(BR.interactor, interactor)
        }
    }
}