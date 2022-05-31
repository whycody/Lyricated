package com.whycody.wordslife.search.lyric.vocabulary.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.data.VocabularyItem
import com.whycody.wordslife.BR
import com.whycody.wordslife.R
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyInteractor

class VocabularyAdapter(private val interactor: VocabularyInteractor): ListAdapter<VocabularyItem,
        VocabularyAdapter.VocabularyHolder>(VocabularyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_vocabulary, parent, false)
        return VocabularyHolder(binding)
    }

    override fun onBindViewHolder(holder: VocabularyHolder, position: Int) = holder.setupData(getItem(position))

    inner class VocabularyHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(vocabularyItem: VocabularyItem) {
            binding.setVariable(BR.vocabularyItem, vocabularyItem)
            binding.setVariable(BR.interactor, interactor)
        }
    }
}