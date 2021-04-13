package com.whycody.wordslife.search

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.R
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment(), IOnBackPressed {

    private var searchWord = ""
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        checkSavedInstanceState(savedInstanceState)
        return view
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) return
        searchViewModel.searchWord(searchWord)
    }

    override fun onBackPressed(): Boolean {
        val scrollBounds = Rect()
        view?.getHitRect(scrollBounds)
        return if(!view!!.findViewById<View>(R.id.searchFieldFragment).getLocalVisibleRect(scrollBounds)) {
            view!!.searchAppBar.setExpanded(true)
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