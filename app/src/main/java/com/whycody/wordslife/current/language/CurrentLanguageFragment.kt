package com.whycody.wordslife.current.language

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.choose.language.ChooseLanguageFragment
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentCurrentLanguageBinding
import org.koin.android.ext.android.inject

class CurrentLanguageFragment : Fragment() {

    private val languageDao: LanguageDao by inject()
    private lateinit var binding: FragmentCurrentLanguageBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var rotateAnimation: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCurrentLanguageBinding.inflate(inflater)
        sharedPrefs = context?.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)!!
        rotateAnimation = AnimationUtils.loadAnimation(context!!, R.anim.arrows_rotation)
        observeCurrentLanguages()
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        with(binding) {
            mainLanguageLayout.setOnClickListener { startChooseLanguageFragment(true) }
            translationLanguageLayout.setOnClickListener { startChooseLanguageFragment(false) }
            switchLangsArrows.setOnClickListener { switchLangsArrowsBtnClicked() }
        }
    }

    private fun switchLangsArrowsBtnClicked() {
        languageDao.switchCurrentLanguages()
        binding.switchLangsArrows.rotation = 0f
        binding.switchLangsArrows.startAnimation(rotateAnimation)
    }

    private fun startChooseLanguageFragment(mainLanguage: Boolean) =
        (activity as MainActivity).navigateTo(ChooseLanguageFragment().newInstance(mainLanguage))

    private fun observeCurrentLanguages() {
        observeMainLanguage()
        observeTranslationLanguage()
    }

    private fun observeMainLanguage() =
        SharedPreferenceStringLiveData(sharedPrefs,
                LanguageDaoImpl.MAIN_LANGUAGE, LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE)
                .observe(activity as MainActivity, {
                    binding.mainLanguage = languageDao.getLanguage(it)
                })

    private fun observeTranslationLanguage() =
        SharedPreferenceStringLiveData(sharedPrefs,
                LanguageDaoImpl.TRANSLATION_LANGUAGE, LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)
                .observe(activity as MainActivity, {
                    binding.translationLanguage = languageDao.getLanguage(it)
                })

}