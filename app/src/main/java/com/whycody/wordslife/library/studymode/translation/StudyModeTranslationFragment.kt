package com.whycody.wordslife.library.studymode.translation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentStudyModeTranslationBinding
import com.whycody.wordslife.library.studymode.StudyModeViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class StudyModeTranslationFragment : Fragment() {

    private lateinit var typeOfPhrase: String
    private lateinit var binding: FragmentStudyModeTranslationBinding
    private val studyModeViewModel: StudyModeViewModel by sharedViewModel()
    private val lyricTranslationViewModel: LyricTranslationViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStudyModeTranslationBinding.inflate(inflater)
        typeOfPhrase = arguments?.getString(LyricTranslationFragment.TYPE_OF_PHRASE,
            LyricTranslationFragment.MAIN_PHRASE)!!
        addHeader()
        observeNumberOfShownWords()
        observeExtendedLyricItem()
        observeTranslationItem()
        return binding.root
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.headerContainer,
            HeaderFragment.newInstance(getHeaderText()))
        fragmentTransaction.commit()
    }

    private fun getHeaderText() =
        if(typeOfPhrase == LyricTranslationFragment.MAIN_PHRASE) getString(R.string.lyric)
        else getString(R.string.translation)

    private fun observeNumberOfShownWords() =
        studyModeViewModel.getNumberOfShownWords().observe(viewLifecycleOwner) {
            val translationIsShown = typeOfPhrase == LyricTranslationFragment.TRANSLATION_PHRASE ||
                    it == studyModeViewModel.getNumberOfAvailableWords().value!!
            binding.translationIsShown = translationIsShown
            if(it == studyModeViewModel.getNumberOfAvailableWords().value!!) startPulseAnim()
        }

    private fun startPulseAnim() =
        binding.flagImage.startAnimation(AnimationUtils
            .loadAnimation(requireContext(), R.anim.flag_pulse_anim))

    private fun observeTranslationItem() = lyricTranslationViewModel.getTranslationItem()
        .observe(viewLifecycleOwner) { binding.translationItem = it }

    private fun observeExtendedLyricItem() = studyModeViewModel.getExtendedLyricItem()
        .observe(viewLifecycleOwner) {
            if(it==null) return@observe
            lyricTranslationViewModel.findTranslation(it, typeOfPhrase)
        }

    companion object {
        fun newInstance(typeOfPhrase: String): StudyModeTranslationFragment {
            val fragment = StudyModeTranslationFragment()
            with(Bundle()) {
                putString(LyricTranslationFragment.TYPE_OF_PHRASE, typeOfPhrase)
                fragment.arguments = this
            }
            return fragment
        }
    }

}