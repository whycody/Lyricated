package com.whycody.wordslife.search

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.R
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment(), IOnBackPressed {

    private var searchWord = ""
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        if(savedInstanceState == null) searchViewModel.searchWord(searchWord)
        return view
    }

    override fun onBackPressed(): Boolean {
        val nestedScroll = view as NestedScrollView
        val scrollBounds = Rect()
        nestedScroll.getHitRect(scrollBounds)
        return if(!view!!.findViewById<View>(R.id.searchFieldFragment).getLocalVisibleRect(scrollBounds)) {
            nestedScroll.smoothScrollTo(0, 0, 1000)
            false
        } else true
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!activity?.isChangingConfigurations!!) resetSearchWord()
    }

    private fun resetSearchWord() = searchViewModel.searchWord("")

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