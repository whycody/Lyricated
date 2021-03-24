package com.whycody.wordslife.search

import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class SearchViewModel(private val lastSearchRepository: LastSearchRepository,
                      private val languageDao: LanguageDao): ViewModel() {

    fun searchWord(word: String) = lastSearchRepository.insertLastSearch(getLastSearch(word))

    private fun getLastSearch(word: String) = LastSearch(
            mainLanguageId = languageDao.getCurrentMainLanguage().id,
            translationLanguageId = languageDao.getCurrentTranslationLanguage().id,
            text = word)
}