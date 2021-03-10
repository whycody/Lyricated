package com.whycody.wordslife.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var layoutView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        val searchWord = arguments?.getString(SEARCH_WORD, "")!!
        binding.searchWord = searchWord
        layoutView = binding.root
        return layoutView
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