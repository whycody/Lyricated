package com.whycody.wordslife.search.translation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.main.MainActivity
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.databinding.FragmentTranslationBinding
import com.whycody.wordslife.search.SearchViewModel
import com.whycody.wordslife.search.translation.recycler.TranslationAdapter
import com.whycody.wordslife.search.translation.recycler.TranslationItemDecoration
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TranslationFragment : Fragment(), TranslationInteractor {

    private var job: Job? = null
    private val languageDao: LanguageDao by inject()
    private val translationViewModel: TranslationViewModel by viewModel()
    private val searchViewModel: SearchViewModel by sharedViewModel()
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentTranslationBinding.inflate(inflater)
        sharedPrefs = requireActivity().applicationContext.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.translationViewModel = translationViewModel
        setupRecycler(binding.translationRecycler)
        observeLoading()
        observeFindLyricsResponse()
        observeFindLyricsResponseReady()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun setupRecycler(recyclerView: RecyclerView) {
        val translationAdapter = TranslationAdapter(this)
        observeTranslations(translationAdapter)
        with(recyclerView) {
            adapter = translationAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(TranslationItemDecoration(requireContext()))
        }
    }

    private fun observeFindLyricsResponse() = searchViewModel.getFindLyricsResponse()
        .observe(viewLifecycleOwner) { translationViewModel.submitTranslations(it.translations) }

    private fun observeFindLyricsResponseReady() = searchViewModel.getFindLyricsResponseReady()
        .observe(viewLifecycleOwner) { translationViewModel.setResultsReady(it) }

    private fun observeTranslations(adapter: TranslationAdapter) = translationViewModel
        .getTranslations().observe(viewLifecycleOwner) {
            if (it == adapter.currentList) return@observe
            adapter.submitList(it)
            searchViewModel.setTranslations(it)
        }

    private fun observeLoading() = translationViewModel
        .getLoading().observe(activity as MainActivity) {
            searchViewModel.setTranslationsLoading(it)
        }

    override fun translationClicked(translation: Translation) {
        if(translation.translationLangId != languageDao.getCurrentMainLanguage().id)
            languageDao.setCurrentMainLanguage(translation.translationLangId!!)
        searchViewModel.setCurrentInputText(translation.translatedPhrase!!)
        searchViewModel.searchWord(translation.translatedPhrase)
    }
}