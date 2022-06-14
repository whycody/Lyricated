package com.whycody.wordslife.search.lyric

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.databinding.FragmentLyricBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.lyric.movie.MovieFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationFragment
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyFragment
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LyricFragment : BottomSheetDialogFragment() {

    private var extendedLyricItem: ExtendedLyricItem? = null
    private var job: Job? = null
    private var searchWord = ""
    private lateinit var binding: FragmentLyricBinding
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLyricBinding.inflate(inflater)
        extendedLyricItem = arguments?.getSerializable(EXTENDED_LYRIC)!! as ExtendedLyricItem
        lyricViewModel.setExtendedLyricItem(extendedLyricItem!!)
        binding.lyricHeader.setOnClickListener{ dismiss() }
        checkSavedInstanceState(savedInstanceState)
        observeExtendedLyricItem()
        observeSearchWord()
        return binding.root
    }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null) return
        addFragments()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun addFragments() {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentsContainer,
            LyricTranslationFragment.newInstance(LyricTranslationFragment.MAIN_PHRASE))
        fragmentTransaction.add(R.id.fragmentsContainer,
            LyricTranslationFragment.newInstance(LyricTranslationFragment.TRANSLATION_PHRASE))
        fragmentTransaction.add(R.id.fragmentsContainer, MovieFragment())
        fragmentTransaction.add(R.id.fragmentsContainer, VocabularyFragment())
        fragmentTransaction.commit()
    }

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(activity as MainActivity) {
            binding.lyricId = it.lyricId
        }

    private fun observeSearchWord() = searchViewModel.getSearchWord()
            .observe(activity as MainActivity) {
                if(searchWord.isEmpty()) searchWord = it
                else dismiss()
            }

    companion object {
        const val EXTENDED_LYRIC = "extendedLyric"
        fun newInstance(extendedLyricItem: ExtendedLyricItem) =
            LyricFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTENDED_LYRIC, extendedLyricItem)
                }
            }
    }
}