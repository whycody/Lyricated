package com.whycody.wordslife.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(private val lastSearchRepository: LastSearchRepository,
                    private val languageDao: LanguageDao):
        ViewModel(), HistoryInteractor {

    private lateinit var historyItemsFlow: Flow<List<LastSearch>>
    private val historyItems = MutableLiveData<List<HistoryItem>>()
    private val clickedWord = MutableLiveData<String>()

    fun loadHistoryItems(all: Boolean = false, onlySaved: Boolean = false) {
        setHistoryItemsFlowValue(all, onlySaved)
        viewModelScope.launch {
            collectHistoryItems()
        }
    }

    private fun setHistoryItemsFlowValue(all: Boolean, onlySaved: Boolean) {
        historyItemsFlow = if(!all) lastSearchRepository.flowFourLastSearches()
        else if(!onlySaved) lastSearchRepository.flowAllLastSearches()
        else lastSearchRepository.flowAllSavedLastSearches()
    }

    private suspend fun collectHistoryItems() = flowHistoryItems().collect {
        historyItems -> this.historyItems.postValue(historyItems)
    }

    private fun flowHistoryItems(): Flow<List<HistoryItem>> = historyItemsFlow.map { list ->
        list.map { getHistoryItemFromLastSearch(it) }
    }

    private fun getHistoryItemFromLastSearch(lastSearch: LastSearch) =
            HistoryItem(lastSearch.id, lastSearch.text,
                    languageDao.getLanguage(lastSearch.mainLanguageId)!!.drawable,
                    languageDao.getLanguage(lastSearch.translationLanguageId)!!.drawable,
                    lastSearch.saved)

    fun getHistoryItems() = historyItems

    fun getClickedWord() = clickedWord

    fun resetClickedWord() = clickedWord.postValue("")

    override fun onHistoryItemClick(historyItem: HistoryItem) {
        val lastSearch = lastSearchRepository.getLastSearchById(historyItem.id)
        clickedWord.postValue(lastSearch.text)
        updateCurrentLanguages(lastSearch)
        postNewValues(lastSearch)
    }

    private fun updateCurrentLanguages(lastSearch: LastSearch) =
        languageDao.setCurrentLanguages(lastSearch.mainLanguageId, lastSearch.translationLanguageId)

    private fun postNewValues(lastSearch: LastSearch) = clickedWord.postValue(lastSearch.text)

    override fun onStarClick(historyItem: HistoryItem) =
        lastSearchRepository.updateLastSearchSaved(historyItem.id, !historyItem.saved)

}