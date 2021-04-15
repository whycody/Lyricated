package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class SearchViewModel(private val lastSearchRepository: LastSearchRepository,
                      private val languageDao: LanguageDao): ViewModel() {

    private val searchWord = MutableLiveData<String>()
    private val userAction = MutableLiveData<Int>()

    fun searchWord(word: String) {
        searchWord.value = word
        if(word.isEmpty()) return
        val lastSearch = getLastSearch(word)
        val exactLastSearch = getExactLastSearch(lastSearch)
        if(exactLastSearch != null) lastSearchRepository.refreshTime(exactLastSearch.id)
        else lastSearchRepository.insertLastSearch(lastSearch)
    }

    fun getSearchWord() = searchWord

    fun setUserAction(id: Int) = userAction.postValue(id)

    fun getUserAction() = userAction

    fun resetUserAction() = userAction.postValue(SearchFragment.NO_ACTION)

    private fun getExactLastSearch(lastSearch: LastSearch) =
            lastSearchRepository.getAllLastSearches().find { lastSearchesAreTheSame(it, lastSearch) }

    private fun lastSearchesAreTheSame(lsOne: LastSearch, lsTwo: LastSearch) =
            lsOne.text == lsTwo.text && lsOne.mainLanguageId == lsTwo.mainLanguageId &&
                    lsOne.translationLanguageId == lsTwo.translationLanguageId

    private fun getLastSearch(word: String) = LastSearch(
            mainLanguageId = languageDao.getCurrentMainLanguage().id,
            translationLanguageId = languageDao.getCurrentTranslationLanguage().id,
            text = word)
}