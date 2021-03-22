package com.whycody.wordslife.search.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.BR
import com.whycody.wordslife.R

class SearchAdapter: ListAdapter<LyricItem, SearchAdapter.SearchHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.item_search, parent, false)
        return SearchHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.setupData(getItem(position))
    }

    inner class SearchHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(lyricItem: LyricItem) {
            binding.setVariable(BR.position, layoutPosition)
            binding.setVariable(BR.mainText, lyricItem.mainLangSentence)
            binding.setVariable(BR.translation, lyricItem.translatedSentence)
            binding.executePendingBindings()
        }
    }
}