package com.whycody.wordslife.search.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whycody.wordslife.R
import com.whycody.wordslife.search.result.SearchResultFragment

class SearchContentFragment: Fragment() {

    private val mainLyricsSearchResultFragment =
        SearchResultFragment.newInstance(SearchResultFragment.MAIN_LYRICS)
    private val similarLyricsSearchResultFragment =
        SearchResultFragment.newInstance(SearchResultFragment.SIMILAR_LYRICS)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_content, container, false)
        if(savedInstanceState == null) addFragments()
        return view
    }

    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentsContainer, mainLyricsSearchResultFragment)
        fragmentTransaction.add(R.id.fragmentsContainer, similarLyricsSearchResultFragment)
        fragmentTransaction.commit()
    }
}