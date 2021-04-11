package com.whycody.wordslife.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.search.result.SearchResultFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {

    private lateinit var layoutView: View
    private var searchWord = ""
    private val searchViewModel: SearchViewModel by sharedViewModel()

    private val mainLyricsSearchResultFragment =
            SearchResultFragment.newInstance(SearchResultFragment.MAIN_LYRICS)
    private val similarLyricsSearchResultFragment =
            SearchResultFragment.newInstance(SearchResultFragment.SIMILAR_LYRICS)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        checkSavedInstanceState(savedInstanceState)
        binding.lifecycleOwner = activity
        binding.viewModel = searchViewModel
        layoutView = binding.root
        return layoutView
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            addFragments()
            searchViewModel.searchWord(searchWord)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!activity?.isChangingConfigurations!!) resetSearchWord()
    }

    private fun resetSearchWord() = searchViewModel.searchWord("")
    
    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentsContainer, mainLyricsSearchResultFragment)
        fragmentTransaction.add(R.id.fragmentsContainer, similarLyricsSearchResultFragment)
        fragmentTransaction.commit()
    }

    companion object {
        const val SEARCH_WORD = "search_word"

        fun newInstance(searchWord: String): SearchFragment {
            val fragment = SearchFragment()
            with(Bundle()) {
                putString(SEARCH_WORD, searchWord)
                fragment.arguments = this
            }
            return fragment
        }
    }
}