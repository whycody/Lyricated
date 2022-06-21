package com.whycody.lyricated.search.configuration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao
import com.whycody.lyricated.databinding.FragmentConfigurationItemsBinding
import com.whycody.lyricated.search.configuration.recycler.ConfigurationItemAdapter
import com.whycody.lyricated.search.translation.recycler.TranslationItemDecoration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationItemsFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationItemsBinding
    private val confViewModel: ConfigurationViewModel by viewModel()
    private val searchConfDao: SearchConfigurationDao by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentConfigurationItemsBinding.inflate(inflater)
        observeSearchConf()
        setupConfigurationRecycler()
        return binding.root
    }

    private fun observeSearchConf() =
        searchConfDao.getSearchConfigurationLiveData().observe(requireActivity()) {
            confViewModel.searchConfUpdated()
        }

    private fun setupConfigurationRecycler() {
        val confItemAdapter = ConfigurationItemAdapter(confViewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        with(binding.configurationRecycler) {
            adapter = confItemAdapter
            this.layoutManager = layoutManager
            addItemDecoration(TranslationItemDecoration(requireContext()))
        }
        observeConfItems(confItemAdapter)
    }

    private fun observeConfItems(adapter: ConfigurationItemAdapter) =
        confViewModel.getConfItems().observe(requireActivity()) {
            if (it.isEmpty()) binding.configurationRecycler.visibility = View.GONE
            else binding.configurationRecycler.visibility = View.VISIBLE
            adapter.submitList(it)
        }

}