package com.whycody.wordslife.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.MainNavigation
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.databinding.FragmentHomeBinding
import com.whycody.wordslife.home.history.HistoryAdapter
import com.whycody.wordslife.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var layoutView: View
    private val languageDao: LanguageDao by inject()
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false)
        layoutView = binding.root
        observeSearchedWord()
        setupSearchWordInput()
        setupRecycler(binding)
        startAnimations()
        return layoutView
    }

    private fun observeSearchedWord() = homeViewModel.getSearchedWord().observe(activity as MainActivity, {
        if(it != "") {
            searchWordFromHistoryItem(it)
            homeViewModel.resetWord()
        }
    })

    private fun setupSearchWordInput() =
            layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                    searchTypedWord()
                true
            }

    private fun searchWordFromHistoryItem(text: String) {
        (activity as MainNavigation).navigateTo(SearchFragment().newInstance(text))
        layoutView.searchWordInput.setText("")
    }

    private fun searchTypedWord() {
        hideKeyboard()
        val currentLastSearchItem = getCurrentLastSearchItem()
        (activity as MainNavigation).navigateTo(SearchFragment().newInstance(currentLastSearchItem.text))
        homeViewModel.insertHistoryItem(currentLastSearchItem)
        layoutView.searchWordInput.setText("")
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun getCurrentLastSearchItem() = LastSearch(
        mainLanguageId = languageDao.getCurrentMainLanguage().id,
        translationLanguageId = languageDao.getCurrentTranslationLanguage().id,
        text = layoutView.searchWordInput.text.toString()
    )

    private fun setupRecycler(binding: FragmentHomeBinding) {
        val historyAdapter = HistoryAdapter(homeViewModel)
        observeHistoryItems(binding, historyAdapter)
        binding.historyDisponible = true
        with(binding.root.historyRecycler) {
            itemAnimator?.changeDuration = 0
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(binding: FragmentHomeBinding, historyAdapter: HistoryAdapter) =
        homeViewModel.getHistoryItems().observe(activity as MainActivity, {
            binding.historyDisponible = it.isNotEmpty()
            if (historyAdapter.currentList.isEmpty())
                layoutView.historyRecycler.scheduleLayoutAnimation()
            historyAdapter.submitList(it)
        })

    private fun startAnimations() {
        with(layoutView) {
            bannerStarsOne.startAnimation(AnimationUtils
                    .loadAnimation(activity?.applicationContext, R.anim.fade_stars_one))
            bannerStarsTwo.startAnimation(AnimationUtils
                    .loadAnimation(activity?.applicationContext, R.anim.fade_stars_two))
            bannerStarsThree.startAnimation(AnimationUtils
                    .loadAnimation(activity?.applicationContext, R.anim.fade_stars_three))
            bannerStarsFour.startAnimation(AnimationUtils
                    .loadAnimation(activity?.applicationContext, R.anim.fade_stars_four))
        }
    }

}