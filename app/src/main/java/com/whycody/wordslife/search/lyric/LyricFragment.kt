package com.whycody.wordslife.search.lyric

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.whycody.wordslife.MainActivity
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SharedPreferenceStringLiveData
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.databinding.FragmentLyricBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.lyric.movie.MovieFragment
import com.whycody.wordslife.search.lyric.quote.QuoteFragment
import com.whycody.wordslife.search.lyric.translation.LyricTranslationFragment
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyFragment
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LyricFragment : BottomSheetDialogFragment() {

    private var lyricId = 0
    private var job: Job? = null
    private var searchWord = ""
    private val lyricViewModel: LyricViewModel by sharedViewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentLyricBinding.inflate(inflater)
        lyricId = arguments?.getInt(LYRIC_ID, 0)!!
        binding.lyricHeader.setOnClickListener{ dismiss() }
        checkSavedInstanceState(savedInstanceState)
        observeCurrentLanguages()
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
        fragmentTransaction.add(R.id.fragmentsContainer, QuoteFragment())
        fragmentTransaction.add(R.id.fragmentsContainer, MovieFragment())
        fragmentTransaction.add(R.id.fragmentsContainer, VocabularyFragment())
        fragmentTransaction.add(R.id.fragmentsContainer, LyricTranslationFragment())
        fragmentTransaction.commit()
    }

    private fun observeCurrentLanguages() {
        val sharedPrefs: SharedPreferences = activity!!.applicationContext
                .getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        observeMainLanguage(sharedPrefs)
        observeTranslationLanguage(sharedPrefs)
    }

    private fun observeMainLanguage(sharedPrefs: SharedPreferences) {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.MAIN_LANGUAGE,
                LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE).observe(activity as MainActivity, {
            lyricViewModel.setLyricLanguages(LyricLanguages(it,
                    sharedPrefs.getString(LanguageDaoImpl.TRANSLATION_LANGUAGE,
                            LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)!!))
        })
    }

    private fun observeTranslationLanguage(sharedPrefs: SharedPreferences) {
        SharedPreferenceStringLiveData(sharedPrefs, LanguageDaoImpl.TRANSLATION_LANGUAGE,
                LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE).observe(activity as MainActivity, {
            lyricViewModel.setLyricLanguages(LyricLanguages(
                    sharedPrefs.getString(LanguageDaoImpl.MAIN_LANGUAGE,
                            LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE)!!, it))
        })
    }

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