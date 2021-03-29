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
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.search.result.SearchResultFragment
import kotlinx.android.synthetic.main.fragment_home.view.searchWordInput
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
        if(savedInstanceState == null) addFragments()
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        binding.lifecycleOwner = activity
        binding.viewModel = searchViewModel
        layoutView = binding.root
        setupSearchWordInput()
        observeSearchWord()
        if(savedInstanceState==null)
            searchViewModel.searchWord(searchWord)
        return layoutView
    }

    private fun setupSearchWordInput() =
            layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
                val searchWord = layoutView.searchWordInput.text.toString()
                if(actionId == EditorInfo.IME_ACTION_SEARCH && wordIsCorrect(searchWord))
                    searchViewModel.searchWord(searchWord)
                true
            }

    private fun wordIsCorrect(word: String) =
            word.trim().replace(Regex("[*.?]"), "").isNotEmpty()
    
    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.searchContainer, mainLyricsSearchResultFragment)
        fragmentTransaction.add(R.id.searchContainer, similarLyricsSearchResultFragment)
        fragmentTransaction.commit()
    }

    private fun observeSearchWord() =
            searchViewModel.getSearchWord().observe(activity as MainActivity, { hideKeyboard() })

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) ?: return
        (imm as InputMethodManager).hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
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