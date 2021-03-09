package com.whycody.wordslife.choose.language.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.BR
import com.whycody.wordslife.R
import com.whycody.wordslife.choose.language.ChooseLanguageInteractor
import com.whycody.wordslife.data.Language

class ChooseLanguageAdapter(private val chooseLanguageInteractor: ChooseLanguageInteractor):
    ListAdapter<Language, ChooseLanguageAdapter.CurrentLanguageHolder>(
    ChooseLanguageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentLanguageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_language, parent, false)
        return CurrentLanguageHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentLanguageHolder, position: Int) {
        holder.setupData(getItem(position))
    }

    inner class CurrentLanguageHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(language: Language) {
            binding.setVariable(BR.language, language)
            binding.setVariable(BR.chooseLanguageInteractor, chooseLanguageInteractor)
            binding.setVariable(BR.position, layoutPosition)
            binding.executePendingBindings()
        }
    }
}