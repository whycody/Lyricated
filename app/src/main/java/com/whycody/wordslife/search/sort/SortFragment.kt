package com.whycody.wordslife.search.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.SearchConfiguration
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.databinding.FragmentSortBinding
import com.whycody.wordslife.search.sort.recycler.SortItemAdapter
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

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