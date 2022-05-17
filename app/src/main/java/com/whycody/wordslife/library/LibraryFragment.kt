package com.whycody.wordslife.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.data.library.LibraryDaoImpl
import com.whycody.wordslife.databinding.FragmentLibraryBinding
import com.whycody.wordslife.library.history.HistoryFragment
import com.whycody.wordslife.library.recycler.LibraryAdapter
import com.whycody.wordslife.library.recycler.LibraryInteractor
import org.koin.android.viewmodel.ext.android.viewModel

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
        }
    }
}