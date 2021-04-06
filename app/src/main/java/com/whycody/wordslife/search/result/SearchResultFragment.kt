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
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentSearchResultBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.result.recycler.SearchResultAdapter
import kotlinx.android.synthetic.main.fragment_search_result.view.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchResultFragment : Fragment() {

    private lateinit var layoutView: View
    private lateinit var typeOfLyrics: String
    private var job: Job? = null
    private val searchResultViewModel: SearchResultViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchResultBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search_result, container, false)
        typeOfLyrics = arguments?.getString(TYPE_OF_LYRICS, MAIN_LYRICS)!!
        searchResultViewModel.setTypeOfLyrics(typeOfLyrics)
        layoutView = binding.root
        binding.searchViewModel = searchResultViewModel
        binding.lifecycleOwner = activity as MainActivity
        setupRecycler()
        observeCurrentLanguages()
        observeSearchWord()
        return layoutView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        job = MainScope().launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { searchResultViewModel.collectLyricItems() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun setupRecycler() {
        with(SearchResultAdapter(searchResultViewModel, activity as MainActivity)) {
            layoutView.searchResultRecycler.adapter = this
            layoutView.searchResultRecycler.itemAnimator?.changeDuration = 0
            observeLyrics(this)
        }
    }

    private fun observeLyrics(resultAdapter: SearchResultAdapter) {
        searchResultViewModel.getLyricItems().observe(activity as MainActivity, {
            resultAdapter.submitList(it)
            if(resultAdapter.currentList.isEmpty() || resultShownAgain(resultAdapter.currentList, it))
                layoutView.searchResultRecycler.scheduleLayoutAnimation()
        })
    }

    private fun resultShownAgain(currentLyricItems: List<LyricItem>, newLyricItems: List<LyricItem>) =
            currentLyricItems.isNotEmpty() && newLyricItems.isNotEmpty() &&
                    currentLyricItems[0].lyricId == newLyricItems[0].lyricId &&
                    newLyricItems.size <= currentLyricItems.size && newLyricItems.size != 1

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

    private fun observeSearchWord() = searchViewModel.getSearchWord()
            .observe(activity as MainActivity, { searchResultViewModel.searchWord(it) })

    companion object {
        const val TYPE_OF_LYRICS = "type of lyrics"
        const val MAIN_LYRICS = "main lyrics"
        const val SIMILAR_LYRICS = "similar lyrics"

        fun newInstance(typeOfLyrics: String): SearchResultFragment {
            val searchResultFragment = SearchResultFragment()
            with(Bundle()) {
                putString(TYPE_OF_LYRICS, typeOfLyrics)
                searchResultFragment.arguments = this
            }
            return searchResultFragment
        }
    }
}