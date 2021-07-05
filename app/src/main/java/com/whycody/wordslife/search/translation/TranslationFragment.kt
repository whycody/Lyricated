package com.whycody.wordslife.search.translation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentTranslationBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.translation.recycler.TranslationAdapter
import com.whycody.wordslife.search.translation.recycler.TranslationItemDecoration
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class TranslationFragment : Fragment(), TranslationInteractor {

    private var job: Job? = null
    private val languageDao: LanguageDao by inject()
    private val translationViewModel: TranslationViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentTranslationBinding.inflate(inflater)
        sharedPrefs = activity!!.applicationContext.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        binding.lifecycleOwner = activity
        binding.translationViewModel = translationViewModel
        setupRecycler(binding.translationRecycler)
        observeLoading()
        observeCurrentLanguages()
        observeSearchWord()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        job = MainScope().launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { translationViewModel.collectTranslations() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        val translationAdapter = TranslationAdapter(this)
        observeTranslations(translationAdapter)
        with(recyclerView) {
            adapter = translationAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(TranslationItemDecoration(context!!.applicationContext))
        }
    }

    private fun observeTranslations(adapter: TranslationAdapter) = translationViewModel
        .getTranslations().observe(activity as MainActivity, {
                if(it == adapter.currentList) return@observe
                adapter.submitList(it)
                searchViewModel.setTranslations(it)
            })

    private fun observeLoading() = translationViewModel
            .getLoading().observe(activity as MainActivity, {
                searchViewModel.setTranslationsLoading(it)
            })

    private fun observeCurrentLanguages() {
        observeMainLanguage()
        observeTranslationLanguage()
    }

    private fun observeMainLanguage() {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.MAIN_LANGUAGE,
                LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE).observe(activity as MainActivity, {
            translationViewModel.setLyricLanguages(LyricLanguages(it,
                    sharedPrefs.getString(LanguageDaoImpl.TRANSLATION_LANGUAGE,
                            LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)!!))
                })
    }

    private fun observeTranslationLanguage() {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.TRANSLATION_LANGUAGE,
                LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE).observe(activity as MainActivity, {
            translationViewModel.setLyricLanguages(LyricLanguages(sharedPrefs.getString(
                    LanguageDaoImpl.MAIN_LANGUAGE, LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE)!!, it))
                })
    }

    private fun observeSearchWord() = searchViewModel.getSearchWord()
        .observe(activity as MainActivity) {
            translationViewModel.searchWord(it)
        }

    override fun translationClicked(translation: Translation) {
        languageDao.switchCurrentLanguages()
        searchViewModel.searchWord(translation.translatedPhrase!!)
    }
}