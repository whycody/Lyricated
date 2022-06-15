package com.whycody.wordslife.search.lyric.translation

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.utilities.TextCopyUtility
import com.whycody.wordslife.databinding.FragmentLyricTranslationBinding
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LyricTranslationFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var typeOfPhrase: String
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val languageDao: LanguageDao by inject()
    private val lyricTranslationViewModel: LyricTranslationViewModel by viewModel()
    private val textCopyUtility: TextCopyUtility by inject()
    private lateinit var binding: FragmentLyricTranslationBinding
    private lateinit var tts: TextToSpeech

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        typeOfPhrase = arguments?.getString(TYPE_OF_PHRASE, MAIN_PHRASE)!!
        binding = FragmentLyricTranslationBinding.inflate(inflater)
        val view = (parentFragment as BottomSheetDialogFragment).dialog?.window?.decorView!!
        binding.contentText.setOnLongClickListener { textCopyUtility.copyText(view,
            lyricTranslationViewModel.getTranslationItem().value!!.translatedSentence!!) }
        binding.playTTSBtn.setOnClickListener { playTTS() }
        if(savedInstanceState == null) addHeader()
        observeTranslationItem(binding)
        observeExtendedLyricItem()
        return binding.root
    }

    private fun playTTS() {
        binding.playTTSBtn.startAnimation(AnimationUtils.loadAnimation(requireContext(),
            R.anim.speaker_pulse_anim))
        tts.speak(lyricTranslationViewModel.getTranslationItem().value!!.translatedSentence,
            TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getHeaderText()))
        fragmentTransaction.commit()
    }

    private fun getHeaderText() =
        if(typeOfPhrase == MAIN_PHRASE) getString(R.string.lyric)
        else getString(R.string.translation)

    private fun observeTranslationItem(binding: FragmentLyricTranslationBinding) =
        lyricTranslationViewModel.getTranslationItem().observe(viewLifecycleOwner) {
            binding.translationItem = it
        }

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(viewLifecycleOwner) {
            lyricTranslationViewModel.findTranslation(it, typeOfPhrase)
        }

    override fun onInit(status: Int) { configureTTS() }

    private fun configureTTS() {
        tts = TextToSpeech(requireContext(), this)
        val languages = LyricLanguages(languageDao.getCurrentMainLanguage().id,
            languageDao.getCurrentTranslationLanguage().id)
        val locale = getLocale(getCurrentLang(languages))
        binding.ttsAvailable = locale != null
        if(locale != null) tts.language = locale
    }

    private fun getCurrentLang(languages: LyricLanguages) =
        if(typeOfPhrase == MAIN_PHRASE) languages.mainLangId
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
        const val TYPE_OF_PHRASE = "type of phrase"
        const val MAIN_PHRASE = "main phrase"
        const val TRANSLATION_PHRASE = "translation phrase"
        fun newInstance(typeOfPhrase: String): LyricTranslationFragment {
            val fragment = LyricTranslationFragment()
            with(Bundle()) {
                putString(TYPE_OF_PHRASE, typeOfPhrase)
                fragment.arguments = this
            }
            return fragment
        }
    }
}