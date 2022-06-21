package com.whycody.lyricated.search.filter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.lyricated.R
import com.whycody.lyricated.search.filter.choose.source.ChooseSourceActivity
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.databinding.FragmentFilterBinding
import com.whycody.lyricated.search.SearchFragment
import com.whycody.lyricated.search.sort.recycler.SortItemAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment : BottomSheetDialogFragment() {

    private val filterViewModel: FilterViewModel by viewModel()
    private val searchConfDao: SearchConfigurationDao by inject()
    private var lastSearchConf = searchConfDao.getSearchConfiguration()
    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterBinding.inflate(inflater)
        binding.filterHeader.setOnClickListener { dismiss() }
        binding.clearFiltersBtn.setOnClickListener{ filterViewModel.clearFilters() }
        observeSearchConf()
        observeUserAction()
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        val sortItemAdapter = SortItemAdapter(filterViewModel)
        binding.filterRecycler.adapter = sortItemAdapter
        binding.filterRecycler.itemAnimator?.changeDuration = 0
        observeFilterItems(sortItemAdapter)
    }

    private fun observeFilterItems(sortItemAdapter: SortItemAdapter) {
        filterViewModel.getFilterItems().observe(viewLifecycleOwner) {
            sortItemAdapter.submitList(it)
        }
    }

    private fun observeSearchConf() {
        searchConfDao.getSearchConfigurationLiveData().observe(requireActivity()) {
            val currentSearchConf = searchConfDao.getSearchConfiguration()
            binding.filtersAreChanged =
                searchConfDao.getSearchConfiguration().checkedFilters.isNotEmpty()
            if (currentSearchConf.chosenSource != lastSearchConf.chosenSource)
                filterViewModel.refreshSortOptions()
            lastSearchConf = currentSearchConf
        }
    }

    private fun observeUserAction() {
        filterViewModel.getUserAction().observe(requireActivity()) {
            if (it.actionType == SearchFragment.NO_ACTION) return@observe
            startChooseSourceActivity()
            filterViewModel.resetUserAction()
        }
    }

    private fun startChooseSourceActivity() =
        activity?.startActivity(Intent(requireContext(), ChooseSourceActivity::class.java))

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    companion object {
        const val CHOOSE_SOURCE_CLICKED = 2
    }
}