package com.whycody.lyricated.search.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.whycody.lyricated.R
import com.whycody.lyricated.databinding.FragmentSearchContentBinding
import com.whycody.lyricated.search.configuration.ConfigurationItemsFragment
import com.whycody.lyricated.search.result.SearchResultFragment

class SearchContentFragment: Fragment(), SearchContentView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSearchContentBinding.inflate(inflater)
        if(savedInstanceState == null) addFragments()
        return binding.root
    }

    override fun scrollToTop() =
            (view as NestedScrollView).smoothScrollTo(0, 0, 700)

    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentsContainer, ConfigurationItemsFragment())
        fragmentTransaction.add(R.id.fragmentsContainer,
            SearchResultFragment.newInstance(SearchResultFragment.MAIN_LYRICS))
        fragmentTransaction.add(R.id.fragmentsContainer,
            SearchResultFragment.newInstance(SearchResultFragment.SIMILAR_LYRICS))
        fragmentTransaction.commit()
    }
}