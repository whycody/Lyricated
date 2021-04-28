package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.UserAction
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class SearchViewModel(private val lastSearchRepository: LastSearchRepository,
                      private val languageDao: LanguageDao): ViewModel() {

    private val searchWord = MutableLiveData<String>()
    private val userAction = MutableLiveData(UserAction())
    private val mainResultsReady = MutableLiveData(false)
    private val similarResultsReady = MutableLiveData(false)
    private val mainResultsReadyToAdmit = MutableLiveData(false)
    private val similarResultsReadyToAdmit = MutableLiveData(false)

    fun searchWord(word: String) {
        searchWord.value = word
        if(word.isEmpty()) return
        val lastSearch = getLastSearch(word)
        val exactLastSearch = getExactLastSearch(lastSearch)
        if(exactLastSearch != null) lastSearchRepository.refreshTime(exactLastSearch.id)
        else lastSearchRepository.insertLastSearch(lastSearch)
    }

    fun setMainResultsReady(ready: Boolean) {
        mainResultsReady.value = ready
        mainResultsReadyToAdmit.value = mainResultsReady.value!! && similarResultsReady.value!!
        similarResultsReadyToAdmit.value = mainResultsReady.value!! && similarResultsReady.value!!
    }

    fun setSimilarResultsReady(ready: Boolean) {
        similarResultsReady.value = ready
        mainResultsReadyToAdmit.value = mainResultsReady.value!! && similarResultsReady.value!!
        similarResultsReadyToAdmit.value = mainResultsReady.value!! && similarResultsReady.value!!
    }

    fun getMainResultsReadyToAdmit() = mainResultsReadyToAdmit

    fun getSimilarResultsReadyToAdmit() = similarResultsReadyToAdmit

    fun getSearchWord() = searchWord

    fun setUserAction(actionType: Int, actionId: Int) = userAction.postValue(UserAction(actionType, actionId))

    fun getUserAction() = userAction

    fun resetUserAction() = userAction.postValue(UserAction())

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