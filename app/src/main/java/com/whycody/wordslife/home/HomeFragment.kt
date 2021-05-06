package com.whycody.wordslife.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.MainNavigation
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
        observeSearchWord()
        observeClickedWord()
        setupRecycler(binding)
        return binding.root
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