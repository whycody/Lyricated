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
import androidx.recyclerview.widget.LinearLayoutManager
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.search.recycler.SearchAdapter
import kotlinx.android.synthetic.main.fragment_home.view.searchWordInput
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var layoutView: View
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        val searchWord = arguments?.getString(SEARCH_WORD, "")!!
        binding.searchWord = searchWord
        layoutView = binding.root
        searchViewModel.searchWord(getFormattedWord(searchWord))
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
        searchViewModel.searchWord(getFormattedWord(layoutView.searchWordInput.text.toString()))
    }

    private fun getFormattedWord(word: String) = word.toLowerCase()
            .replace("*","").replace("?", "")

    private fun setupRecycler() {
        val adapter = SearchAdapter()
        layoutView.searchResultRecycler.layoutManager = LinearLayoutManager(activity?.applicationContext)
        layoutView.searchResultRecycler.adapter = adapter
        observeLyrics(adapter)
    }

    private fun observeLyrics(adapter: SearchAdapter) {
        searchViewModel.getLyricsItems().observe(activity as MainActivity, {
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