package com.whycody.wordslife.search.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.whycody.wordslife.R
import com.whycody.wordslife.search.result.SearchResultFragment

class SearchContentFragment: Fragment(), SearchContentView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_content, container, false)
        if(savedInstanceState == null) addFragments()
        return view
    }

    override fun scrollToTop() =
            (view as NestedScrollView).smoothScrollTo(0, 0, 700)

    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentsContainer,
            SearchResultFragment.newInstance(SearchResultFragment.MAIN_LYRICS))
        fragmentTransaction.add(R.id.fragmentsContainer,
            SearchResultFragment.newInstance(SearchResultFragment.SIMILAR_LYRICS))
        fragmentTransaction.commit()
    }
}