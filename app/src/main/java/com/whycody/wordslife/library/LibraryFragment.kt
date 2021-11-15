package com.whycody.wordslife.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whycody.wordslife.databinding.FragmentLibraryBinding
import com.whycody.wordslife.library.recycler.LibraryAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private val libraryViewModel: LibraryViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLibraryBinding.inflate(inflater)
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        val adapter = LibraryAdapter(libraryViewModel)
        binding.libraryRecycler.adapter = adapter
        observeLibraryItems(adapter)
    }

    private fun observeLibraryItems(adapter: LibraryAdapter) =
        libraryViewModel.getLibraryItems().observe(requireActivity(), { adapter.submitList(it) })

}