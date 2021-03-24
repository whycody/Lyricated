package com.whycody.wordslife.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import java.util.*

class HomeViewModel(private val lastSearchRepository: LastSearchRepository,
                    private val chooseLanguageRepository: ChooseLanguageRepository):
        ViewModel(), HistoryInteractor {

    private val historyItems = MutableLiveData<List<HistoryItem>>()
    private val searchedWord = MutableLiveData<String>()

    init {
        historyItems.postValue(lastSearchRepository.getHistoryItems())
    }

    fun getHistoryItems() = historyItems

    fun getSearchedWord() = searchedWord

    fun resetWord() = searchedWord.postValue("")

    override fun onHistoryItemClick(historyItem: HistoryItem) {
        val lastSearch = lastSearchRepository.getLastSearchById(historyItem.id)
        searchedWord.postValue(lastSearch.text)
        updateCurrentLanguages(lastSearch)
        refreshTimeInLastSearch(lastSearch)
        postNewValues(lastSearch)
    }

    private fun updateCurrentLanguages(lastSearch: LastSearch) {
        chooseLanguageRepository.setCurrentMainLanguage(lastSearch.mainLanguageId)
        chooseLanguageRepository.setCurrentTranslationLanguage(lastSearch.translationLanguageId)
    }

    private fun refreshTimeInLastSearch(lastSearch: LastSearch) {
        lastSearchRepository.refreshTime(Calendar.getInstance().timeInMillis, lastSearch.id)
    }

    private fun postNewValues(lastSearch: LastSearch) {
        searchedWord.postValue(lastSearch.text)
        historyItems.postValue(lastSearchRepository.getHistoryItems())
    }

    override fun onStarClick(historyItem: HistoryItem) {
        lastSearchRepository.updateLastSearchSaved(historyItem.id, !historyItem.saved)
        historyItems.value = lastSearchRepository.getHistoryItems()
    }
}