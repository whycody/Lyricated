package com.whycody.wordslife.search.lyric.translation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.databinding.FragmentTranslationBinding
import com.whycody.wordslife.search.lyric.LyricViewModel
import com.whycody.wordslife.search.lyric.header.HeaderFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class TranslationFragment : Fragment() {

    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val translationViewModel: TranslationViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentTranslationBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_translation, container, false)
        if(savedInstanceState == null) addHeader()
        observeTranslationItem(binding)
        observeExtendedLyricItem()
        return binding.root
    }

    private fun addHeader() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.headerContainer,
            HeaderFragment.newInstance(getString(R.string.translation)))
        fragmentTransaction.commit()
    }

    private fun observeTranslationItem(binding: FragmentTranslationBinding) =
        translationViewModel.getTranslationItem().observe(activity as MainActivity, {
            binding.translationItem = it
        })

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(activity as MainActivity, {
            translationViewModel.findTranslation(it)
        })

}