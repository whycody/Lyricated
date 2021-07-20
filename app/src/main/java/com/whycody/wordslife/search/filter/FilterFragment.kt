package com.whycody.wordslife.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentFilterBinding

class FilterFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFilterBinding.inflate(inflater)
        binding.filterHeader.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    companion object {
        const val ONLY_MOVIES = "only movies"
        const val ONLY_SERIES = "only series"
        const val WITHOUT_CURSES = "without curses"
        const val WITH_CURSES = "with curses"
    }
}