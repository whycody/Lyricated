package com.whycody.wordslife.library.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.databinding.FragmentHistoryBinding
import com.whycody.wordslife.home.HomeViewModel
import com.whycody.wordslife.home.history.HistoryAdapter
import com.whycody.wordslife.main.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var onlySaved = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentHistoryBinding.inflate(inflater)
        onlySaved = arguments?.getBoolean(ONLY_SAVED, false)!!
        binding.onlySaved = onlySaved
        loadHistoryItems()
        setupRecycler(binding)
        return binding.root
    }

    private fun loadHistoryItems() {
        MainScope().launch {
            delay(150)
            homeViewModel.loadHistoryItems(true, onlySaved)
        }
    }

    private fun setupRecycler(binding: FragmentHistoryBinding) {
        val historyAdapter = HistoryAdapter(homeViewModel)
        observeHistoryItems(binding, historyAdapter)
        with(binding.historyRecycler) {
            itemAnimator?.changeDuration = 0
            adapter = historyAdapter
        }
    }

    private fun observeHistoryItems(binding: FragmentHistoryBinding, historyAdapter: HistoryAdapter) =
        homeViewModel.getHistoryItems().observe(activity as MainActivity, {
            if (historyAdapter.currentList.isEmpty())
                binding.historyRecycler.scheduleLayoutAnimation()
            historyAdapter.submitList(it)
        })

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