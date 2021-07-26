package com.whycody.wordslife.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.databinding.FragmentFilterBinding
import com.whycody.wordslife.search.sort.recycler.SortItemAdapter
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FilterFragment : BottomSheetDialogFragment() {

    private val filterViewModel: FilterViewModel by viewModel()
    private val searchConfDao: SearchConfigurationDao by inject()
    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterBinding.inflate(inflater)
        binding.filterHeader.setOnClickListener { dismiss() }
        searchConfDao.getSearchConfigurationLiveData().observe(requireActivity(), {binding.filtersAreChanged = searchConfDao.getSearchConfiguration().checkedFilters.isNotEmpty()})
        binding.clearFiltersBtn.setOnClickListener{ filterViewModel.clearFilters() }
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        val sortItemAdapter = SortItemAdapter(filterViewModel)
        binding.filterRecycler.adapter = sortItemAdapter
        binding.filterRecycler.itemAnimator?.changeDuration = 80
        observeFilterItems(sortItemAdapter)
    }

    private fun observeFilterItems(sortItemAdapter: SortItemAdapter) {
        filterViewModel.getFilterItems().observe(requireActivity(), {
            sortItemAdapter.submitList(it)
        })
    }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar
}