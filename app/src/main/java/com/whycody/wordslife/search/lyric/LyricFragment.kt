package com.whycody.wordslife.search.lyric

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.databinding.FragmentLyricBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.lyric.movie.MovieFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationFragment
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LyricFragment : BottomSheetDialogFragment() {

    private var lyricId = 0
    private var job: Job? = null
    private var searchWord = ""
    private lateinit var binding: FragmentLyricBinding
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private val searchConfigurationDao: SearchConfigurationDao by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLyricBinding.inflate(inflater)
        lyricId = arguments?.getInt(LYRIC_ID, 0)!!
        binding.lyricHeader.setOnClickListener{ dismiss() }
        checkSavedInstanceState(savedInstanceState)
        observeCurrentLanguages()
        observeExtendedLyricItem()
        observeSearchWord()
        return binding.root
    }

    override fun getTheme() = R.style.Theme_NoWiredStrapInNavigationBar

    private fun checkSavedInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null) return
        lyricViewModel.searchLyricItem(lyricId)
        addFragments()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        job = MainScope().launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) { lyricViewModel.collectExtendedLyricItem() }
        }
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

    private fun observeCurrentLanguages() {
        searchConfigurationDao.getSearchConfigurationLiveData().observe(activity as MainActivity, {
            lyricViewModel.setLyricLanguages(searchConfigurationDao.getLyricLanguages())
        })
    }

    private fun observeExtendedLyricItem() =
        lyricViewModel.getCurrentExtendedLyricItem().observe(activity as MainActivity, {
            binding.lyricId = it.lyricId
        })

    private fun observeSearchWord() = searchViewModel.getSearchWord()
            .observe(activity as MainActivity) {
                if(searchWord.isEmpty()) searchWord = it
                else dismiss()
            }

    companion object {
        const val LYRIC_ID = "lyricId"
        fun newInstance(lyricId: Int) =
            LyricFragment().apply {
                arguments = Bundle().apply {
                    putInt(LYRIC_ID, lyricId)
                }
            }
    }
}