package com.whycody.wordslife.library.studymode.translation

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.utilities.TextCopyUtility
import com.whycody.wordslife.databinding.FragmentStudyModeTranslationBinding
import com.whycody.wordslife.library.studymode.StudyModeViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class StudyModeTranslationFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var typeOfPhrase: String
    private lateinit var binding: FragmentStudyModeTranslationBinding
    private val textCopyUtility: TextCopyUtility by inject()
    private val studyModeViewModel: StudyModeViewModel by sharedViewModel()
    private val lyricTranslationViewModel: LyricTranslationViewModel by viewModel()
    private val languageDao: LanguageDao by inject()
    private lateinit var tts: TextToSpeech

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStudyModeTranslationBinding.inflate(inflater)
        binding.translationText.setOnLongClickListener { copyText() }
        binding.viewAboveTranslation.setOnLongClickListener { false }
        binding.viewAboveTranslation.setOnClickListener { }
        tts = TextToSpeech(requireContext(), this)
        binding.playTTSBtn.setOnClickListener { playTTS() }
        typeOfPhrase = arguments?.getString(LyricTranslationFragment.TYPE_OF_PHRASE,
            LyricTranslationFragment.MAIN_PHRASE)!!
        addHeader()
        observeNumberOfShownWords()
        observeExtendedLyricItem()
        observeTranslationItem()
        return binding.root
    }

    private fun copyText() = textCopyUtility.copyText(requireParentFragment().requireView(),
        lyricTranslationViewModel.getTranslationItem().value!!.translatedSentence!!,
        parentFragment?.view?.findViewById(R.id.revealWordBtn))

    private fun playTTS() {
        binding.playTTSBtn.startAnimation(AnimationUtils.loadAnimation(requireContext(),
            R.anim.speaker_pulse_anim))
        tts.speak(lyricTranslationViewModel.getTranslationItem().value!!.translatedSentence,
            TextToSpeech.QUEUE_FLUSH, null, null)
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

    override fun onInit(status: Int) { configureTTS() }

    private fun configureTTS() {
        val languages = LyricLanguages(languageDao.getCurrentMainLanguage().id,
            languageDao.getCurrentTranslationLanguage().id)
        val locale = getLocale(getCurrentLang(languages))
        binding.ttsAvailable = locale != null
        if(locale != null) tts.language = locale
    }

    private fun getCurrentLang(languages: LyricLanguages) =
        if(typeOfPhrase == LyricTranslationFragment.MAIN_PHRASE) languages.mainLangId
        else languages.translationLangId

    private fun getLocale(lang: String): Locale? {
        return when(lang) {
            LanguageDaoImpl.EN -> Locale.US
            LanguageDaoImpl.DE -> Locale.GERMAN
            LanguageDaoImpl.FR -> Locale.FRENCH
            LanguageDaoImpl.IT -> Locale.ITALIAN
            LanguageDaoImpl.ES -> Locale("spa")
            else -> null
        }
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