package com.whycody.lyricated.search.translation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.BR
import com.whycody.lyricated.R
import com.whycody.lyricated.data.Translation
import com.whycody.lyricated.search.translation.TranslationInteractor
import com.whycody.lyricated.search.translation.TranslationViewModel

class TranslationAdapter(private val interactor: TranslationInteractor):
    ListAdapter<Translation, RecyclerView.ViewHolder>(TranslationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TranslationViewModel.LOADING -> getTranslationLoadingHolder(inflater, parent)
            else -> getTranslationHolder(inflater, parent)
        }
    }

    private fun getTranslationHolder(inflater: LayoutInflater, parent: ViewGroup): TranslationHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.item_translation, parent, false)
        return TranslationHolder(binding)
    }

    private fun getTranslationLoadingHolder(inflater: LayoutInflater, parent: ViewGroup): TranslationHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_translation_loading, parent, false)
        return TranslationHolder(binding)
    }

    override fun getItemViewType(position: Int) = getItem(position).type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as TranslationHolder).setupData(getItem(position))

    inner class TranslationHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(translation: Translation) {
            binding.setVariable(BR.translation, translation)
            binding.setVariable(BR.position, layoutPosition)
            binding.setVariable(BR.interactor, interactor)
            binding.executePendingBindings()
        }
    }

}