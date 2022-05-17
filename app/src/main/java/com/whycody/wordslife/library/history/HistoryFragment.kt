package com.whycody.wordslife.library.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.databinding.FragmentHistoryBinding
import com.whycody.wordslife.home.HistoryInteractor
import com.whycody.wordslife.home.HomeViewModel
import com.whycody.wordslife.home.history.HistoryAdapter
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.search.SearchViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : BottomSheetDialogFragment(), HistoryInteractor {

    private val homeViewModel: HomeViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private var onlySaved = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentHistoryBinding.inflate(inflater)
        binding.sortHeader.setOnClickListener { dismiss() }
        onlySaved = arguments?.getBoolean(ONLY_SAVED, false)!!
        binding.onlySaved = onlySaved
        loadHistoryItems()
        setupRecycler(binding)
        return binding.root
    }

    private fun loadHistoryItems() {
        MainScope().launch {
            homeViewModel.loadHistoryItems(true, onlySaved)
        }
    }

    private fun setupRecycler(binding: FragmentHistoryBinding) {
        val historyAdapter = HistoryAdapter(this)
        observeHistoryItems(historyAdapter)
        with(binding.historyRecycler) {
            itemAnimator?.changeDuration = 0
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(historyAdapter: HistoryAdapter) =
        homeViewModel.getHistoryItems().observe(activity as MainActivity) {
            historyAdapter.submitList(it)
        }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    override fun onHistoryItemClick(historyItem: HistoryItem) {
        homeViewModel.onHistoryItemClick(historyItem)
        searchViewModel.searchWord(historyItem.text)
        dismiss()
    }

    override fun onStarClick(historyItem: HistoryItem) = homeViewModel.onStarClick(historyItem)

    companion object {
        const val ONLY_SAVED = "only saved"
        fun newInstance(onlySaved: Boolean): HistoryFragment {
            val fragment = HistoryFragment()
            with(Bundle()) {
                putBoolean(ONLY_SAVED, onlySaved)
                fragment.arguments = this
            }
            return fragment
        }
    }
}