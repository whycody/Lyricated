package com.whycody.wordslife.search.result

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
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentSearchResultBinding
import com.whycody.wordslife.search.result.recycler.SearchResultAdapter
import kotlinx.android.synthetic.main.fragment_search_result.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchResultFragment : Fragment() {

    private lateinit var layoutView: View
    private lateinit var typeOfLyrics: String
    private val searchResultViewModel: SearchResultViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchResultBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search_result, container, false)
        typeOfLyrics = arguments?.getString(TYPE_OF_LYRICS, MAIN_LYRICS)!!
        searchResultViewModel.typeOfLyrics.value = typeOfLyrics
        layoutView = binding.root
        binding.searchViewModel = searchResultViewModel
        binding.lifecycleOwner = activity as MainActivity
        setupRecycler()
        observeCurrentLanguages()
        return layoutView
    }

    fun searchWord(word: String, typeOfLyrics: String) = searchResultViewModel.searchWord(word, typeOfLyrics)

    private fun setupRecycler() {
        with(SearchResultAdapter()) {
            layoutView.searchResultRecycler.adapter = this
            observeLyrics(this)
        }
    }

    private fun observeLyrics(resultAdapter: SearchResultAdapter) {
        searchResultViewModel.getLyricItems().observe(activity as MainActivity, {
            if(it.isEmpty())
                layoutView.searchResultRecycler.scheduleLayoutAnimation()
            resultAdapter.submitList(it)
        })
    }

    private fun observeCurrentLanguages() {
        val sharedPrefs: SharedPreferences = activity!!.applicationContext
                .getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        observeMainLanguage(sharedPrefs)
        observeTranslationLanguage(sharedPrefs)
    }

    private fun observeMainLanguage(sharedPrefs: SharedPreferences) {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.MAIN_LANGUAGE,
                LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE).observe(activity as MainActivity, {
            searchResultViewModel.setLyricLanguages(LyricLanguages(it,
                    sharedPrefs.getString(LanguageDaoImpl.TRANSLATION_LANGUAGE,
                            LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)!!))
        })
    }

    private fun observeTranslationLanguage(sharedPrefs: SharedPreferences) {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.TRANSLATION_LANGUAGE,
                LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE).observe(activity as MainActivity, {
            searchResultViewModel.setLyricLanguages(LyricLanguages(
                    sharedPrefs.getString(LanguageDaoImpl.MAIN_LANGUAGE,
                            LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE)!!, it))
        })
    }

    fun newInstance(typeOfLyrics: String): SearchResultFragment {
        val fragment = SearchResultFragment()
        with(Bundle()) {
            putString(TYPE_OF_LYRICS, typeOfLyrics)
            fragment.arguments = this
        }
        return fragment
    }

    companion object {
        const val TYPE_OF_LYRICS = "type of lyrics"
        const val MAIN_LYRICS = "main lyrics"
        const val SIMILAR_LYRICS = "similar lyrics"
    }
}