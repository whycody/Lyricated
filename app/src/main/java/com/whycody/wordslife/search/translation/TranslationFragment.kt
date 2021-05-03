package com.whycody.wordslife.search.translation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentTranslationBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.translation.recycler.TranslationAdapter
import com.whycody.wordslife.search.translation.recycler.TranslationItemDecoration
import kotlinx.android.synthetic.main.fragment_translation.view.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class TranslationFragment : Fragment() {

    private var job: Job? = null
    private lateinit var layoutView: View
    private val translationViewModel: TranslationViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentTranslationBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_translation, container, false)
        layoutView = binding.root
        binding.lifecycleOwner = activity
        binding.translationViewModel = translationViewModel
        setupRecycler(layoutView.translationRecycler)
        observeCurrentLanguages()
        observeSearchWord()
        return layoutView
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
        val translationAdapter = TranslationAdapter()
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
                adapter.submitList(it)
            })

    private fun observeCurrentLanguages() {
        val sharedPrefs: SharedPreferences = activity!!.applicationContext
            .getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        observeMainLanguage(sharedPrefs)
        observeTranslationLanguage(sharedPrefs)
    }

    private fun observeMainLanguage(sharedPrefs: SharedPreferences) {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.MAIN_LANGUAGE,
                LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE).observe(activity as MainActivity, {
            translationViewModel.setLyricLanguages(LyricLanguages(it,
                    sharedPrefs.getString(LanguageDaoImpl.TRANSLATION_LANGUAGE,
                            LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)!!))
                })
    }

    private fun observeTranslationLanguage(sharedPrefs: SharedPreferences) {
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
}