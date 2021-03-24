package com.whycody.wordslife.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(private val lastSearchRepository: LastSearchRepository,
                    private val chooseLanguageRepository: ChooseLanguageRepository,
                    private val languageDao: LanguageDao):
        ViewModel(), HistoryInteractor {

    private val historyItemsFlow = lastSearchRepository.flowFourLastSearches()
    private val historyItems = MutableLiveData<List<HistoryItem>>()
    private val searchedWord = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            collectHistoryItems()
        }
    }

    private suspend fun collectHistoryItems() = flowHistoryItems().collect {
        historyItems -> this.historyItems.postValue(historyItems)
    }

    private fun flowHistoryItems(): Flow<List<HistoryItem>> =
            historyItemsFlow.map { list ->
                list.map { getHistoryItemFromLastSearch(it) }
            }

    private fun getHistoryItemFromLastSearch(lastSearch: LastSearch) =
            HistoryItem(lastSearch.id, lastSearch.text,
                    languageDao.getLanguage(lastSearch.mainLanguageId)!!.drawable,
                    languageDao.getLanguage(lastSearch.translationLanguageId)!!.drawable,
                    lastSearch.saved)

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
    }

    override fun onStarClick(historyItem: HistoryItem) {
        lastSearchRepository.updateLastSearchSaved(historyItem.id, !historyItem.saved)
    }
}