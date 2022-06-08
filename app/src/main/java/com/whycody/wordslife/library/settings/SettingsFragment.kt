package com.whycody.wordslife.library.settings

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.data.app.configuration.AppConfigurationDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.settings.SettingsDaoImpl
import com.whycody.wordslife.databinding.FragmentSettingsBinding
import com.whycody.wordslife.search.sort.recycler.SortItemAdapter
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : BottomSheetDialogFragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()
    private val appConfigurationDao: AppConfigurationDao by inject()
    private val searchConfigurationDao: SearchConfigurationDao by inject()
    private val lastSearchRepository: LastSearchRepository by inject()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        binding.settingsHeader.setOnClickListener { dismiss() }
        setupRecycler()
        observeAppConf()
        observeSearchConf()
        observeActionBtnClicked()
        return binding.root
    }

    private fun setupRecycler() {
        val sortItemAdapter = SortItemAdapter(settingsViewModel)
        binding.settingsRecycler.adapter = sortItemAdapter
        binding.settingsRecycler.itemAnimator?.changeDuration = 0
        observeSettingsItems(sortItemAdapter)
    }

    private fun observeSettingsItems(sortItemAdapter: SortItemAdapter) {
        settingsViewModel.getSettingsItems().observe(viewLifecycleOwner) {
            sortItemAdapter.submitList(it)
        }
    }

    private fun observeAppConf() = appConfigurationDao.getAppConfigurationLiveData()
        .observe(viewLifecycleOwner) {
            settingsViewModel.refreshSettingsItems()
            updateAppearance()
        }

    private fun observeActionBtnClicked() = settingsViewModel.getActionBtnClicked()
        .observe(viewLifecycleOwner) {
            if(it == null) return@observe
            val builder = AlertDialog.Builder(ContextThemeWrapper(requireContext(), R.style.AlertDialogCustom))
            builder.setTitle(getTitle(it))
                .setMessage(getString(R.string.are_you_sure))
                .setCancelable(true)
                .setPositiveButton(getString(android.R.string.ok)) { _, _ -> positiveBtnClicked(it) }
                .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }
                .show()
            settingsViewModel.getActionBtnClicked().postValue(null)
        }

    private fun getTitle(actionId: String) =
        if(actionId == SettingsDaoImpl.DELETE_SAVED) getString(R.string.deselectall)
        else getString(R.string.deletehistory)

    private fun positiveBtnClicked(actionId: String) {
        if(actionId == SettingsDaoImpl.DELETE_HISTORY)
            lastSearchRepository.deleteAllLastSearches()
        else lastSearchRepository.deselectAllLastSearches()
    }

    private fun updateAppearance() {
        val currentAppConfig = appConfigurationDao.getAppConfiguration()
        val defaultMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val darkMode = AppCompatDelegate.MODE_NIGHT_YES
        val lightMode = AppCompatDelegate.MODE_NIGHT_NO
        when (currentAppConfig.appearance) {
            SettingsDaoImpl.LIGHT -> AppCompatDelegate.setDefaultNightMode(lightMode)
            SettingsDaoImpl.DARK -> AppCompatDelegate.setDefaultNightMode(darkMode)
            else -> AppCompatDelegate.setDefaultNightMode(defaultMode)
        }
    }

    private fun observeSearchConf() = searchConfigurationDao.getSearchConfigurationLiveData()
        .observe(viewLifecycleOwner) { settingsViewModel.refreshSettingsItems() }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar
}