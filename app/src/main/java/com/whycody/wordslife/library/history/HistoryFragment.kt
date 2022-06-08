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
import com.whycody.wordslife.search.SearchViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : BottomSheetDialogFragment(), HistoryInteractor {

    private val historyViewModel: HistoryViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private lateinit var binding: FragmentHistoryBinding
    private val historyAdapter = HistoryBottomSheetAdapter(this)
    private var onlySaved = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(inflater)
        binding.sortHeader.setOnClickListener { dismiss() }
        onlySaved = arguments?.getBoolean(ONLY_SAVED, false)!!
        binding.onlySaved = onlySaved
        setupRecycler(binding)
        observeHistoryItems()
        return binding.root
    }

    private fun setupRecycler(binding: FragmentHistoryBinding) {
        with(binding.historyRecycler) {
            itemAnimator?.changeDuration = 0
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems() = historyViewModel.getData(onlySaved)
        .observe(viewLifecycleOwner) { historyAdapter.submitData(lifecycle, it) }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    override fun onHistoryItemClick(historyItem: HistoryItem) {
        homeViewModel.onHistoryItemClick(historyItem)
        searchViewModel.searchWord(historyItem.text)
        dismiss()
    }

    override fun onStarClick(position: Int, historyItem: HistoryItem) {
        homeViewModel.onStarClick(position, historyItem)
        historyItem.saved = !historyItem.saved
        historyAdapter.notifyItemChanged(position)
    }

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