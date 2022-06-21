package com.whycody.lyricated.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.lyricated.data.library.LibraryDaoImpl
import com.whycody.lyricated.databinding.FragmentLibraryBinding
import com.whycody.lyricated.library.history.HistoryFragment
import com.whycody.lyricated.library.recycler.LibraryAdapter
import com.whycody.lyricated.library.recycler.LibraryInteractor
import com.whycody.lyricated.library.settings.SettingsFragment
import com.whycody.lyricated.library.studymode.settings.StudyModeSettingsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment(), LibraryInteractor {

    private lateinit var binding: FragmentLibraryBinding
    private val libraryViewModel: LibraryViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLibraryBinding.inflate(inflater)
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        val adapter = LibraryAdapter(this)
        binding.libraryRecycler.adapter = adapter
        observeLibraryItems(adapter)
    }

    private fun observeLibraryItems(adapter: LibraryAdapter) =
        libraryViewModel.getLibraryItems().observe(requireActivity()) { adapter.submitList(it) }

    override fun libraryItemClicked(libraryItemId: String) {
        when(libraryItemId) {
            LibraryDaoImpl.HISTORY ->
                HistoryFragment.newInstance(false).show(childFragmentManager, "History")
            LibraryDaoImpl.SAVED ->
                HistoryFragment.newInstance(true).show(childFragmentManager, "Saved")
            LibraryDaoImpl.STUDY_MODE ->
                StudyModeSettingsFragment().show(childFragmentManager, "Study Mode")
            LibraryDaoImpl.SETTINGS ->
                SettingsFragment().show(childFragmentManager, "Settings")
        }
    }
}