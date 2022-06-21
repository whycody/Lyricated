package com.whycody.lyricated.library.studymode.loading

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.whycody.lyricated.IOnBackPressed
import com.whycody.lyricated.R
import com.whycody.lyricated.data.ExtendedLyricItem
import com.whycody.lyricated.library.studymode.StudyModeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class StudyModeLoadingFragment : Fragment(), IOnBackPressed {

    private val studyModeViewModel: StudyModeViewModel by sharedViewModel()
    private var extendedLyricItem: ExtendedLyricItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        extendedLyricItem = studyModeViewModel.getExtendedLyricItem().value
        observeExtendedLyricItem()
        studyModeViewModel.showNextLyricItem()
        return inflater.inflate(R.layout.fragment_study_mode_loading, container, false)
    }

    override fun onBackPressed(): Boolean {
        parentFragmentManager.popBackStack()
        studyModeViewModel.getLoadingNextLyricItem().postValue(true)
        return true
    }

    private fun observeExtendedLyricItem() = studyModeViewModel.getExtendedLyricItem()
        .observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                if(it != extendedLyricItem) {
                    delay(500)
                    parentFragmentManager.popBackStack()
                } else extendedLyricItem = it
            }
        }
}