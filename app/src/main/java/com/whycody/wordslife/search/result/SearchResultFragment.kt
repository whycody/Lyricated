package com.whycody.wordslife.search.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
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
        searchResultViewModel.getLyricsItems().observe(activity as MainActivity, {
            if(it.isEmpty())
                layoutView.searchResultRecycler.scheduleLayoutAnimation()
            resultAdapter.submitList(it)
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