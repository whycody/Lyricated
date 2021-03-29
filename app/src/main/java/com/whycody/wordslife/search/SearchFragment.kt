package com.whycody.wordslife.search

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.search.result.SearchResultFragment
import kotlinx.android.synthetic.main.fragment_home.view.searchWordInput
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var layoutView: View
    private var searchWord = ""
    private val searchViewModel: SearchViewModel by viewModel()

    private val mainLyricsSearchResultFragment = SearchResultFragment()
    private val similarLyricsSearchResultFragment = SearchResultFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        if(savedInstanceState == null) addFragments()
        binding.searchWord = searchWord
        layoutView = binding.root
        setupSearchWordInput()
        setTypeOfLyrics()
        searchTypedWord(searchWord)
        return layoutView
    }

    private fun setupSearchWordInput() =
            layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                    if(layoutView.searchWordInput.text.toString().trim().isNotEmpty()) searchTypedWord()
                true
            }
    
    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.searchContainer, mainLyricsSearchResultFragment)
        fragmentTransaction.add(R.id.searchContainer, similarLyricsSearchResultFragment)
        fragmentTransaction.commit()
    }

    private fun setTypeOfLyrics() {
        mainLyricsSearchResultFragment.setTypeOfLyrics(SearchResultFragment.MAIN_LYRICS)
        similarLyricsSearchResultFragment.setTypeOfLyrics(SearchResultFragment.SIMILAR_LYRICS)
    }

    private fun searchTypedWord(searchWord: String = layoutView.searchWordInput.text.toString()) {
        hideKeyboard()
        searchViewModel.searchWord(searchWord)
        mainLyricsSearchResultFragment.searchWord(searchWord)
        similarLyricsSearchResultFragment.searchWord(searchWord)
    }
    
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    fun newInstance(searchWord: String): SearchFragment {
        val fragment = SearchFragment()
        with(Bundle()) {
            putString(SEARCH_WORD, searchWord)
            fragment.arguments = this
        }
        return fragment
    }

    companion object {
        const val SEARCH_WORD = "search_word"
    }
}