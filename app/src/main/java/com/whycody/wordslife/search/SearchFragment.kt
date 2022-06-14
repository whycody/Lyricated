package com.whycody.wordslife.search

import android.animation.LayoutTransition
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.appbar.AppBarLayout
import com.whycody.wordslife.IOnBackPressed
import com.whycody.wordslife.R
import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.SearchConfiguration
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.search.lyric.LyricFragment
import com.whycody.wordslife.search.content.SearchContentFragment
import com.whycody.wordslife.search.content.SearchContentView
import com.whycody.wordslife.search.filter.FilterFragment
import com.whycody.wordslife.search.sort.SortFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception

class SearchFragment : Fragment(), IOnBackPressed {

    private var searchWord = ""
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private val searchConfigurationDao: SearchConfigurationDao by inject()
    private lateinit var searchAppBar: AppBarLayout
    private var appBarLastStateIsExpanded = true
    private var lastSearchConf: SearchConfiguration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.sortBtn.setOnClickListener { SortFragment().show(childFragmentManager, "Sort")}
        binding.filterBtn.setOnClickListener { FilterFragment().show(childFragmentManager, "Filter")}
        searchWord = arguments?.getString(SEARCH_WORD, "")!!
        searchAppBar = binding.searchAppBar
        checkSavedInstanceState(savedInstanceState)
        enableAnimation(binding.searchContainer)
        observeSearchWord()
        observeCurrentLanguages()
        observeUserAction()
        return binding.root
    }

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) return
        searchViewModel.searchWord(searchWord)
        tryShowFragment(SearchContentFragment())
    }

    private fun enableAnimation(linearLayout: LinearLayout) =
            linearLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

    private fun observeSearchWord() = searchViewModel.getSearchWord().observe(viewLifecycleOwner) {
        if (it == null) return@observe
        if (!currentFragmentIsSearchContent()) scrollToTopTheWholeFragment()
        searchViewModel.tryFindLyricsFromApi()
        searchWord = it
    }

    private fun observeCurrentLanguages() = searchConfigurationDao.getSearchConfigurationLiveData()
        .observe(viewLifecycleOwner) {
            val searchConf = searchConfigurationDao.getSearchConfiguration()
            if (lastSearchConf != null && lastSearchConf != searchConf)
                searchViewModel.tryFindLyricsFromApi()
            lastSearchConf = searchConf
        }

    private fun observeUserAction() = searchViewModel.getUserAction().observe(viewLifecycleOwner) {
        if (it.actionType == NO_ACTION) return@observe
        if (it.actionType == LYRIC_CLICKED) tryShowLyricFragment(it.clickedExtendedLyricItem!!)
        appBarLastStateIsExpanded = appBarIsExpanded()
        searchViewModel.resetUserAction()
    }

    private fun tryShowLyricFragment(extendedLyricItem: ExtendedLyricItem) {
        try { LyricFragment.newInstance(extendedLyricItem)
            .show(childFragmentManager, "Lyric") }
        catch (_: Exception) { }
    }

    private fun tryShowFragment(fragment: Fragment) {
        try { showFragment(fragment)
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
        return if(!appBarIsExpanded()) {
            scrollToTopTheWholeFragment()
            false
        } else true
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
        searchAppBar.postDelayed({ searchAppBar.setExpanded(true) }, 100)
        if(childFragmentManager.fragments.any { it is SearchContentFragment })
                (childFragmentManager.fragments.find { it is SearchContentFragment } as SearchContentView).scrollToTop()
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