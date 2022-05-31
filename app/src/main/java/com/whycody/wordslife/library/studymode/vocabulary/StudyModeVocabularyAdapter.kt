package com.whycody.wordslife.library.studymode.vocabulary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.BR
import com.whycody.wordslife.R
import com.whycody.wordslife.data.ExtendedVocabularyItem

class StudyModeVocabularyAdapter: ListAdapter<ExtendedVocabularyItem,
        StudyModeVocabularyAdapter.StudyModeVocabularyHolder>(StudyModeVocabularyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyModeVocabularyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.item_study_mode_vocabulary, parent, false)
        return StudyModeVocabularyHolder(binding)
    }

    override fun onBindViewHolder(holder: StudyModeVocabularyHolder, position: Int) = holder.setupData(getItem(position))

    inner class StudyModeVocabularyHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

        fun setupData(extendedVocabularyItem: ExtendedVocabularyItem) {
            binding.setVariable(BR.word, extendedVocabularyItem.word)
            binding.setVariable(BR.shown, extendedVocabularyItem.shown)
        }
    }
}