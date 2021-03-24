package com.whycody.wordslife.search

import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class SearchViewModel(private val lastSearchRepository: LastSearchRepository): ViewModel() {

    fun searchWord(word: String) = lastSearchRepository.insertLastSearch(word)
}