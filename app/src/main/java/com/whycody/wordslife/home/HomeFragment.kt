package com.whycody.wordslife.home

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.main.MainNavigation
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentHomeBinding
import com.whycody.wordslife.home.history.HistoryAdapter
import com.whycody.wordslife.search.SearchFragment
import com.whycody.wordslife.search.SearchViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.headerText.text = getStringSpannable(getString(R.string.previous_words),
            getString(R.string.previous))
        binding.searchFirstPhraseText.text = getStringSpannable(getString(R.string.search_first_phrase),
            getString(R.string.first))
        homeViewModel.loadHistoryItems()
        setupRecycler(binding)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeSearchWord()
        observeClickedWord()
    }

    private fun getStringSpannable(phrase: String, word: String): SpannableStringBuilder {
        val spanBuilder = SpannableStringBuilder(phrase)
        val indexStart = phrase.indexOf(word)
        val indexEnd = indexStart + word.length
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        spanBuilder.setSpan(ForegroundColorSpan(typedValue.data),
            indexStart, indexEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spanBuilder
    }

    private fun observeSearchWord() = searchViewModel.getSearchWord()
        .observe(activity as MainActivity) {
            if (shouldOpenSearchFragment(it))
                (activity as MainNavigation).navigateTo(SearchFragment.newInstance(it))
        }

    private fun shouldOpenSearchFragment(word: String) =
        !(activity as MainNavigation).fragmentsContainSearchFragment() && word.isNotEmpty()

    private fun observeClickedWord() = homeViewModel.getClickedWord()
        .observe(activity as MainActivity) {
            if (it.isNotEmpty()) {
                searchViewModel.searchWord(it)
                homeViewModel.resetClickedWord()
            }
        }

    private fun setupRecycler(binding: FragmentHomeBinding) {
        val historyAdapter = HistoryAdapter(homeViewModel)
        observeHistoryItems(binding, historyAdapter)
        with(binding.historyRecycler) {
            itemAnimator?.changeDuration = 0
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(binding: FragmentHomeBinding, historyAdapter: HistoryAdapter) =
        homeViewModel.getHistoryItems().observe(activity as MainActivity) {
            if (historyAdapter.currentList.isEmpty())
                binding.historyRecycler.scheduleLayoutAnimation()
            binding.historyIsAvailable = !it.isNullOrEmpty()
            historyAdapter.submitList(it)
        }
}