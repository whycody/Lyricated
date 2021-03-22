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
import com.whycody.wordslife.search.recycler.SearchAdapter
import kotlinx.android.synthetic.main.fragment_home.view.searchWordInput
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var layoutView: View
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private var searchWord = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        this.binding = binding
        layoutView = binding.root
        binding.searchViewModel = searchViewModel
        binding.lifecycleOwner = activity as MainActivity
        searchViewModel.searchWord(searchWord)
        setupSearchWordInput()
        setupRecycler()
        return layoutView
    }

    private fun setupSearchWordInput() =
            layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                    searchTypedWord()
                true
            }

    private fun searchTypedWord() {
        hideKeyboard()
        searchWord = layoutView.searchWordInput.text.toString()
        searchViewModel.searchWord(searchWord)
    }

    private fun setupRecycler() {
        with(SearchAdapter()) {
            layoutView.searchResultRecycler.adapter = this
            observeLyrics(this)
        }
    }

    private fun observeLyrics(adapter: SearchAdapter) {
        searchViewModel.getLyricsItems().observe(activity as MainActivity, {
            if(it.isEmpty())
                layoutView.searchResultRecycler.scheduleLayoutAnimation()
            adapter.submitList(it)
        })
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