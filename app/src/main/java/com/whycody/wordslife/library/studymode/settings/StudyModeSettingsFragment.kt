package com.whycody.wordslife.library.studymode.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentStudyModeSettingsBinding
import com.whycody.wordslife.library.studymode.StudyModeFragment
import com.whycody.wordslife.main.MainNavigation
import com.whycody.wordslife.search.sort.recycler.SortItemAdapter
import org.koin.android.ext.android.inject

class StudyModeSettingsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentStudyModeSettingsBinding
    private val studyModeSettingsViewModel: StudyModeSettingsViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentStudyModeSettingsBinding.inflate(inflater)
        binding.studyModeSettingsHeader.setOnClickListener { dismiss() }
        binding.nextBtn.setOnClickListener { showStudyModeFragment() }
        setupRecycler()
        return binding.root
    }

    private fun showStudyModeFragment() {
        (activity as MainNavigation).navigateTo(StudyModeFragment(), true)
        dismiss()
    }

    private fun setupRecycler() {
        val sortItemAdapter = SortItemAdapter(studyModeSettingsViewModel)
        binding.studyModeRecycler.adapter = sortItemAdapter
        binding.studyModeRecycler.itemAnimator?.changeDuration = 0
        observeStudyModeSettingsItems(sortItemAdapter)
    }

    private fun observeStudyModeSettingsItems(sortItemAdapter: SortItemAdapter) =
        studyModeSettingsViewModel.getStudyModeSettingsItems().observe(viewLifecycleOwner)
        { sortItemAdapter.submitList(it) }


    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar
}