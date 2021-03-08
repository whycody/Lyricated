package com.whycody.wordslife.searchfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.databinding.FragmentSearchBinding
import com.whycody.wordslife.searchfragment.history.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var layoutView: View
    private val languageDao: LanguageDao by inject()
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)
        layoutView = binding.root
        setupSearchWordInput()
        setupRecycler(binding)
        startAnimations()
        return layoutView
    }

    private fun setupSearchWordInput() =
        layoutView.searchWordInput.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
                searchViewModel.insertHistoryItem(getCurrentLastSearchItem())
            true
        }

    private fun getCurrentLastSearchItem() = LastSearch(
            mainLanguageId = languageDao.getCurrentMainLanguage().id,
            translationLanguageId = languageDao.getCurrentTranslationLanguage().id,
            text = layoutView.searchWordInput.text.toString())

    private fun setupRecycler(binding: FragmentSearchBinding) {
        val historyAdapter = HistoryAdapter()
        observeHistoryItems(binding, historyAdapter)
        binding.historyDisponible = true
        with(binding.root.historyRecycler) {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(binding: FragmentSearchBinding, historyAdapter: HistoryAdapter) =
        searchViewModel.getHistoryItems().observe(activity as MainActivity, {
            binding.historyDisponible = it.isNotEmpty()
            historyAdapter.submitList(it)
        })

    private fun startAnimations() {
        val animationOne = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_one)
        val animationTwo = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_two)
        val animationThree = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_three)
        val animationFour = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_stars_four)
        layoutView.bannerStarsOne.startAnimation(animationOne)
        layoutView.bannerStarsTwo.startAnimation(animationTwo)
        layoutView.bannerStarsThree.startAnimation(animationThree)
        layoutView.bannerStarsFour.startAnimation(animationFour)
    }

}