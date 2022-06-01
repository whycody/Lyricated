package com.whycody.wordslife.library.studymode.vocabulary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.whycody.wordslife.R
import com.whycody.wordslife.data.ExtendedVocabularyItem
import com.whycody.wordslife.databinding.FragmentStudyModeVocabularyBinding
import com.whycody.wordslife.library.studymode.StudyModeViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class StudyModeVocabularyFragment : Fragment() {

    private lateinit var binding: FragmentStudyModeVocabularyBinding
    private val vocabularyViewModel: VocabularyViewModel by inject()
    private val studyModeViewModel: StudyModeViewModel by sharedViewModel()
    private lateinit var adapter: StudyModeVocabularyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStudyModeVocabularyBinding.inflate(inflater)
        adapter = StudyModeVocabularyAdapter(studyModeViewModel)
        addHeader()
        setupRecycler()
        observeExtendedLyricItem()
        observeVocabularyItems()
        observeShownWords()
        return binding.root
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getString(R.string.vocabulary)))
        fragmentTransaction.commit()
    }

    private fun setupRecycler() {
        with(adapter) {
            binding.studyModeVocabularyRecycler.adapter = this
            binding.studyModeVocabularyRecycler.itemAnimator = null
            binding.studyModeVocabularyRecycler.layoutManager = getFlexboxLayoutManager()
        }
    }

    private fun getFlexboxLayoutManager(): FlexboxLayoutManager {
        val flexboxManager = FlexboxLayoutManager(context)
        flexboxManager.flexDirection = FlexDirection.ROW
        flexboxManager.justifyContent = JustifyContent.FLEX_START
        return flexboxManager
    }

    private fun observeExtendedLyricItem() = studyModeViewModel.getExtendedLyricItem()
        .observe(viewLifecycleOwner) {
            if(it==null) return@observe
            vocabularyViewModel.findVocabulary(it)
        }

    private fun observeVocabularyItems() = vocabularyViewModel.getVocabularyItems()
        .observe(viewLifecycleOwner) { refreshVocabulary() }

    private fun observeShownWords() = studyModeViewModel.getShownWords()
        .observe(viewLifecycleOwner) { refreshVocabulary() }

    private fun refreshVocabulary() {
        adapter.submitList(vocabularyViewModel.getVocabularyItems().value!!
            .map { ExtendedVocabularyItem(it.index, it.word,
                studyModeViewModel.getShownWords().value!!.contains(it.index)) })
    }
}