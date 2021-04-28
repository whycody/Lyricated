package com.whycody.wordslife.search.result

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var resultAdapter: SearchResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchResultBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search_result, container, false)
        typeOfLyrics = arguments?.getString(TYPE_OF_LYRICS, MAIN_LYRICS)!!
        searchResultViewModel.setTypeOfLyrics(typeOfLyrics)
        resultAdapter = SearchResultAdapter(searchResultViewModel, searchViewModel, activity as MainActivity)
        layoutView = binding.root
        binding.searchViewModel = searchResultViewModel
        binding.lifecycleOwner = activity as MainActivity
        setupRecycler(layoutView.searchResultRecycler)
        observeHidden()
        observeCurrentLanguages()
        observeSearchWord()
        observeResults()
        return layoutView
    }

    private fun observeResults() {
        if(typeOfLyrics == MAIN_LYRICS) observeMainResults()
        else observeSimilarResults()
    }

    private fun observeMainResults() {
        observeMainResultsReady()
        observeMainResultsReadyToAdmit()
    }

    private fun observeSimilarResults() {
        observeSimilarResultsReady()
        observeSimilarResultsReadyToAdmit()
    }

    private fun observeMainResultsReady() = searchResultViewModel.getMainResultsReady()
            .observe(activity as MainActivity) { searchViewModel.setMainResultsReady(it) }

    private fun observeMainResultsReadyToAdmit() = searchViewModel.getMainResultsReadyToAdmit()
            .observe(activity as MainActivity) { if(it) postNewValues() }

    private fun observeSimilarResultsReady() = searchResultViewModel.getSimilarResultsReady()
            .observe(activity as MainActivity) { searchViewModel.setSimilarResultsReady(it) }

    private fun observeSimilarResultsReadyToAdmit() = searchViewModel.getSimilarResultsReadyToAdmit()
            .observe(activity as MainActivity) { if(it) postNewValues() }

    private fun postNewValues() = searchResultViewModel.postNewValues()

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

    private fun setupRecycler(recyclerView: RecyclerView) {
        with(resultAdapter) {
            recyclerView.adapter = this
            recyclerView.itemAnimator = null
            observeLyrics()
        }
    }

    private fun observeHidden() {
        searchResultViewModel.resultsHidden.observe(activity as MainActivity, {
            view?.headerArrow?.rotation = if(it) 0f else 180f
            view?.headerArrow?.animate()?.rotationBy(180f)?.setDuration(200)?.start()
        })
    }

    private fun observeLyrics() = searchResultViewModel.getLyricItems()
            .observe(activity as MainActivity, {
                submitLyricItems(it)
            })

    private fun submitLyricItems(lyricItems: List<LyricItem>) {
        checkIfShouldScrollToTop(resultAdapter.currentList, lyricItems)
        resultAdapter.submitList(lyricItems)
        if(resultAdapter.currentList.isEmpty() || resultShownAgain(resultAdapter.currentList, lyricItems))
            layoutView.searchResultRecycler.scheduleLayoutAnimation()
    }

    private fun checkIfShouldScrollToTop(currentLyricItems: List<LyricItem>, newLyricItems: List<LyricItem>) {
        val listsAreFilled = listsAreNotEmpty(currentLyricItems, newLyricItems)
        if(listsAreFilled && currentLyricItems[0].lyricId != newLyricItems[0].lyricId && typeOfLyrics == MAIN_LYRICS)
            layoutView.searchResultRecycler.smoothScrollToPosition(0)
    }

    private fun resultShownAgain(currentLyricItems: List<LyricItem>, newLyricItems: List<LyricItem>) =
            listsAreNotEmpty(currentLyricItems, newLyricItems) &&
                    currentLyricItems[0].lyricId == newLyricItems[0].lyricId &&
                    newLyricItems.size <= currentLyricItems.size && newLyricItems.size != 1

    private fun listsAreNotEmpty(currentLyricItems: List<LyricItem>, newLyricItems: List<LyricItem>) =
            currentLyricItems.isNotEmpty() && newLyricItems.isNotEmpty()

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