package com.whycody.lyricated.search.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.lyricated.R
import com.whycody.lyricated.data.SearchConfiguration
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.databinding.FragmentSortBinding
import com.whycody.lyricated.search.sort.recycler.SortItemAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SortFragment : BottomSheetDialogFragment() {

    private val sortViewModel: SortViewModel by viewModel()
    private val searchConfigDao: SearchConfigurationDao by inject()
    private lateinit var lastSearchConfig: SearchConfiguration
    private lateinit var binding: FragmentSortBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View {
        binding = FragmentSortBinding.inflate(inflater)
        binding.sortHeader.setOnClickListener { dismiss() }
        lastSearchConfig = searchConfigDao.getSearchConfiguration()
        searchConfigDao.getSearchConfigurationLiveData().observe(requireActivity()) {
            if (lastSearchConfig != searchConfigDao.getSearchConfiguration()) dismiss()
        }
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        val sortItemAdapter = SortItemAdapter(sortViewModel)
        binding.sortRecycler.adapter = sortItemAdapter
        observeSortItems(sortItemAdapter)
    }

    private fun observeSortItems(adapter: SortItemAdapter) {
        sortViewModel.getSortItems().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar
}