package com.whycody.wordslife.search.result

import android.animation.LayoutTransition
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.databinding.FragmentSearchResultBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.result.recycler.SearchResultAdapter
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var typeOfLyrics: String
    private var job: Job? = null
    private val searchResultViewModel: SearchResultViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private val searchConfigurationDao: SearchConfigurationDao by inject()
    private lateinit var resultAdapter: SearchResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSearchResultBinding.inflate(inflater)
        typeOfLyrics = arguments?.getString(TYPE_OF_LYRICS, MAIN_LYRICS)!!
        searchResultViewModel.setTypeOfLyrics(typeOfLyrics)
        resultAdapter = SearchResultAdapter(searchResultViewModel, searchViewModel, activity as MainActivity)
        binding.searchViewModel = searchResultViewModel
        binding.lifecycleOwner = activity as MainActivity
        enableAnimation(binding.root as LinearLayout)
        setupRecycler(binding.searchResultRecycler)
        observeValues()
        return binding.root
    }

    private fun enableAnimation(linearLayout: LinearLayout) =
            linearLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

    private fun observeValues() {
        observeHidden()
        observeCurrentLanguages()
        observeSearchWord()
        observeTranslations()
        observeResults()
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

    private fun postNewValues() {
        searchResultViewModel.setupTranslations()
        searchResultViewModel.postNewValues()
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

    private fun setupRecycler(recyclerView: RecyclerView) {
        with(resultAdapter) {
            recyclerView.adapter = this
            recyclerView.itemAnimator = null
            observeLyrics()
        }
    }

    private fun observeHidden() {
        searchResultViewModel.resultsHidden.observe(activity as MainActivity, {
            binding.headerArrow.rotation = if(it) 0f else 180f
            binding.headerArrow.animate()?.rotationBy(180f)?.setDuration(200)?.start()
        })
    }

    private fun observeLyrics() = searchResultViewModel.getLyricItems()
            .observe(activity as MainActivity, {
                submitLyricItems(it)
            })

    private fun submitLyricItems(lyricItems: List<LyricItem>) {
        checkIfShouldScrollToTop(resultAdapter.currentList, lyricItems)
        resultAdapter.submitList(lyricItems)
    }

    private fun checkIfShouldScrollToTop(currentLyricItems: List<LyricItem>, newLyricItems: List<LyricItem>) {
        val listsAreFilled = listsAreNotEmpty(currentLyricItems, newLyricItems)
        if(listsAreFilled && currentLyricItems[0].lyricId != newLyricItems[0].lyricId && typeOfLyrics == MAIN_LYRICS)
            binding.searchResultRecycler.smoothScrollToPosition(0)
    }

    private fun listsAreNotEmpty(currentLyricItems: List<LyricItem>, newLyricItems: List<LyricItem>) =
            currentLyricItems.isNotEmpty() && newLyricItems.isNotEmpty()

    private fun observeCurrentLanguages() {
        searchConfigurationDao.getSearchConfigurationLiveData().observe(activity as MainActivity, {
            searchResultViewModel.setSearchConfig(searchConfigurationDao.getSearchConfiguration())
        })
    }

    private fun observeSearchWord() = searchViewModel.getSearchWord()
            .observe(activity as MainActivity, { searchResultViewModel.searchWord(it) })

    private fun observeTranslations() = searchViewModel.getTranslations()
            .observe(activity as MainActivity) { searchResultViewModel.applyTranslations(it) }

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