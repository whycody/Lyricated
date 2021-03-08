package com.whycody.wordslife.current.language

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentCurrentLanguageBinding
import org.koin.android.ext.android.inject

class CurrentLanguageFragment : Fragment() {

    private val languageDao: LanguageDao by inject()
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentCurrentLanguageBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_current_language, container, false)
        sharedPrefs = context?.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)!!
        observeCurrentLanguages(binding)
        return binding.root
    }

    private fun observeCurrentLanguages(binding: FragmentCurrentLanguageBinding) {
        observeMainLanguage(binding)
        observeTranslationLanguage(binding)
    }

    private fun observeMainLanguage(binding: FragmentCurrentLanguageBinding) =
        SharedPreferenceStringLiveData(sharedPrefs,
                LanguageDaoImpl.MAIN_LANGUAGE, LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE)
                .observe(activity as MainActivity, {
                    binding.mainLanguage = languageDao.getLanguage(it)
                })

    private fun observeTranslationLanguage(binding: FragmentCurrentLanguageBinding) =
        SharedPreferenceStringLiveData(sharedPrefs,
                LanguageDaoImpl.TRANSLATION_LANGUAGE, LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)
                .observe(activity as MainActivity, {
                    binding.translationLanguage = languageDao.getLanguage(it)
                })

}