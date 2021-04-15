package com.whycody.wordslife.search

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.search.lyric.LyricFragment
import com.whycody.wordslife.search.content.SearchContentFragment
import com.whycody.wordslife.search.content.SearchContentView
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.Exception

class SearchFragment : Fragment(), IOnBackPressed {

    private var searchWord = ""
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private var appBarLastStateIsExpanded = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        checkSavedInstanceState(savedInstanceState)
        observeSearchWord()
        observeUserAction()
        return view
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) return
        searchViewModel.searchWord(searchWord)
        tryShowFragment(SearchContentFragment())
    }

    private fun observeSearchWord() = searchViewModel.getSearchWord().observe(activity as MainActivity, {
        if(it == searchWord) return@observe
        if(!currentFragmentIsSearchContent()) {
            backToSearchContentFragment()
            scrollToTopTheWholeFragment()
        }
        searchWord = it
    })

    private fun observeUserAction() = searchViewModel.getUserAction().observe(activity as MainActivity, {
        if(it.actionType == NO_ACTION) return@observe
        if(it.actionType == LYRIC_CLICKED) showLyricFragment(it.actionId)
        appBarLastStateIsExpanded = appBarIsExpanded()
        searchViewModel.resetUserAction()
    })

    private fun showLyricFragment(lyricId: Int) {
        tryShowFragment(LyricFragment.newInstance(lyricId))
        if(!appBarIsExpanded())
            searchAppBar.setExpanded(true)
    }

    private fun tryShowFragment(fragment: Fragment) {
        try {
            showFragment(fragment)
        } catch (_: Exception) { }
    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim, R.anim.enter_anim, R.anim.exit_anim)
            .add(R.id.fragmentsContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed(): Boolean {
        return if(!currentFragmentIsSearchContent()) {
            backToSearchContentFragment()
            false
        } else if(!appBarIsExpanded()) {
            scrollToTopTheWholeFragment()
            false
        } else true
    }

    private fun backToSearchContentFragment() {
        childFragmentManager.popBackStackImmediate()
        checkIfShouldExpandAppBar()
    }

    private fun checkIfShouldExpandAppBar() {
        if(!currentFragmentIsSearchContent() || appBarLastStateIsExpanded) return
        searchAppBar.setExpanded(false)
    }

    private fun currentFragmentIsSearchContent() =
            try{ childFragmentManager.fragments.last() is SearchContentFragment }
            catch(_: Exception) { true }

    private fun appBarIsExpanded(): Boolean {
        val scrollBounds = Rect()
        view?.getHitRect(scrollBounds)
        return view?.findViewById<View>(R.id.searchFieldFragment)?.getLocalVisibleRect(scrollBounds)?:true
    }

    private fun scrollToTopTheWholeFragment() {
        searchAppBar.setExpanded(true)
        (childFragmentManager.fragments.last() as SearchContentView).scrollToTop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!activity?.isChangingConfigurations!!) resetSearchWord()
    }

    private fun resetSearchWord() = searchViewModel.searchWord("")

    companion object {
        const val SEARCH_WORD = "search_word"
        const val NO_ACTION = 0
        const val LYRIC_CLICKED = 1
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