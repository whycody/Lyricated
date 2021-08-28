package com.whycody.wordslife.home

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        binding.headerText.text = getHeaderTextStringSpannable()
        setupRecycler(binding)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        observeSearchWord()
        observeClickedWord()
    }

    private fun getHeaderTextStringSpannable(): SpannableStringBuilder {
        val prevWords = getString(R.string.previous_words)
        val prev = getString(R.string.previous)
        val spanBuilder = SpannableStringBuilder(prevWords)
        val indexStart = prevWords.indexOf(prev)
        val indexEnd = indexStart + prev.length
        spanBuilder.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.light_blue)),
            indexStart, indexEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        return spanBuilder
    }

    private fun observeSearchWord() = searchViewModel.getSearchWord()
        .observe(activity as MainActivity, {
            if(shouldOpenSearchFragment(it))
                (activity as MainNavigation).navigateTo(SearchFragment.newInstance(it))
    })

    private fun shouldOpenSearchFragment(word: String) =
        !(activity as MainNavigation).fragmentsContainSearchFragment() && word.isNotEmpty()

    private fun observeClickedWord() = homeViewModel.getClickedWord()
        .observe(activity as MainActivity, {
            if(it.isNotEmpty()) {
                searchViewModel.searchWord(it)
                homeViewModel.resetClickedWord()
            }
        })

    private fun setupRecycler(binding: FragmentHomeBinding) {
        val historyAdapter = HistoryAdapter(homeViewModel)
        observeHistoryItems(binding, historyAdapter)
        binding.historyDisponible = true
        with(binding.historyRecycler) {
            itemAnimator?.changeDuration = 0
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(binding: FragmentHomeBinding, historyAdapter: HistoryAdapter) =
        homeViewModel.getHistoryItems().observe(activity as MainActivity, {
            binding.historyDisponible = it.isNotEmpty()
            if (historyAdapter.currentList.isEmpty())
                binding.historyRecycler.scheduleLayoutAnimation()
            historyAdapter.submitList(it)
        })

}