package com.whycody.lyricated.library.studymode.vocabulary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whycody.lyricated.BR
import com.whycody.lyricated.R
import com.whycody.lyricated.data.ExtendedVocabularyItem
import com.whycody.lyricated.data.VocabularyItem
import com.whycody.lyricated.search.lyric.vocabulary.VocabularyInteractor

class StudyModeVocabularyAdapter(private val interactor: VocabularyInteractor):
    ListAdapter<ExtendedVocabularyItem,
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
            binding.setVariable(BR.vocabularyItem, VocabularyItem(layoutPosition, extendedVocabularyItem.word))
            binding.setVariable(BR.interactor, interactor)
            binding.setVariable(BR.shown, extendedVocabularyItem.shown)
        }
    }
}