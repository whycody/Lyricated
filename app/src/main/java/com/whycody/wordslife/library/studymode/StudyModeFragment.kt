package com.whycody.wordslife.library.studymode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentStudyModeBinding
import com.whycody.wordslife.library.studymode.loading.StudyModeLoadingFragment
import com.whycody.wordslife.library.studymode.translation.StudyModeTranslationFragment
import com.whycody.wordslife.library.studymode.vocabulary.StudyModeVocabularyFragment
import com.whycody.wordslife.main.MainNavigation
import com.whycody.wordslife.search.lyric.translation.LyricTranslationFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StudyModeFragment : Fragment(), IOnBackPressed {

    private lateinit var binding: FragmentStudyModeBinding
    private val studyModeViewModel: StudyModeViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        observeLoadingNextLyricItem()
        checkSavedInstanceState(savedInstanceState)
        binding = FragmentStudyModeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = studyModeViewModel
        return binding.root
    }

    override fun onBackPressed(): Boolean {
        studyModeViewModel.getLoadingNextLyricItem().postValue(true)
        return true
    }

    private fun observeLoadingNextLyricItem() = studyModeViewModel.getLoadingNextLyricItem()
        .observe(viewLifecycleOwner) {
            if(it) (activity as MainNavigation).navigateTo(StudyModeLoadingFragment())
        }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null) return
        addFragments()
    }

    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentsContainer,
            StudyModeTranslationFragment.newInstance(LyricTranslationFragment.TRANSLATION_PHRASE))
        fragmentTransaction.add(R.id.fragmentsContainer,
            StudyModeTranslationFragment.newInstance(LyricTranslationFragment.MAIN_PHRASE))
        fragmentTransaction.add(R.id.fragmentsContainer, StudyModeVocabularyFragment())
        fragmentTransaction.commit()
    }

}