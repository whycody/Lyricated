package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.UserAction
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao

class SearchViewModel(private val lastSearchRepository: LastSearchRepository,
                      private val searchConfigurationDao: SearchConfigurationDao): ViewModel() {

    private val searchWord = MutableLiveData<String>()
    private val userAction = MutableLiveData(UserAction())
    private val translations = MutableLiveData<List<Translation>>()
    private val translationsLoading = MutableLiveData(false)
    private val mainResultsReady = MutableLiveData(false)
    private val similarResultsReady = MutableLiveData(false)
    private val mainResultsReadyToAdmit = MutableLiveData(false)
    private val similarResultsReadyToAdmit = MutableLiveData(false)

    fun searchWord(word: String) {
        searchWord.value = word
        if(word.isNotEmpty()) insertLastSearch(word)
    }

    fun setTranslations(translations: List<Translation>) {
        this.translations.value = translations
    }

    private fun insertLastSearch(word: String) {
        val lastSearch = getLastSearch(word)
        val exactLastSearch = getExactLastSearch(lastSearch)
        if(exactLastSearch != null) lastSearchRepository.refreshTime(exactLastSearch.id)
        else lastSearchRepository.insertLastSearch(lastSearch)
    }

    fun setTranslationsLoading(loading: Boolean) {
        translationsLoading.value = loading
        setResultsReadiness()
    }

    fun setMainResultsReady(ready: Boolean) {
        mainResultsReady.value = ready
        setResultsReadiness()
    }

    fun setSimilarResultsReady(ready: Boolean) {
        similarResultsReady.value = ready
        setResultsReadiness()
    }

    private fun setResultsReadiness() {
        val resultsAreReady = resultsAreReady()
        mainResultsReadyToAdmit.value = resultsAreReady
        similarResultsReadyToAdmit.value = resultsAreReady
    }

    private fun resultsAreReady() = mainResultsReady.value!! && similarResultsReady.value!! && !translationsLoading.value!!

    fun getMainResultsReadyToAdmit() = mainResultsReadyToAdmit

    fun getSimilarResultsReadyToAdmit() = similarResultsReadyToAdmit

    fun getTranslations() = translations

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
            mainLanguageId = searchConfigurationDao.getLyricLanguages().mainLangId,
            translationLanguageId = searchConfigurationDao.getLyricLanguages().translationLangId,
            text = word)
}