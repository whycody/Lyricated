package com.whycody.wordslife.search.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentSortBinding

class SortFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View {
        val binding = FragmentSortBinding.inflate(inflater)
        binding.sortHeader.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    companion object {
        const val BEST_MATCH = "best_match"
        const val SHORTEST = "shortest"
        const val LONGEST = "longest"
    }
}