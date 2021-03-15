package com.whycody.wordslife.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.ChooseLanguageRepository
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class HomeViewModel(private val lastSearchRepository: LastSearchRepository,
                    private val chooseLanguageRepository: ChooseLanguageRepository):
        ViewModel(), HistoryInteractor {

    private val historyItems = MutableLiveData<List<HistoryItem>>()
    private val searchedWord = MutableLiveData<String>()

    init {
        historyItems.postValue(lastSearchRepository.getHistoryItems())
    }

    fun insertHistoryItem(lastSearch: LastSearch) {
        lastSearchRepository.insertLastSearch(lastSearch)
        historyItems.postValue(lastSearchRepository.getHistoryItems())
    }

    fun getHistoryItems() = historyItems

    fun getSearchedWord() = searchedWord

    fun resetWord() = searchedWord.postValue("")

    override fun onHistoryItemClick(historyItem: HistoryItem) {
        val lastSearch = lastSearchRepository.getLastSearchById(historyItem.id)
        chooseLanguageRepository.setCurrentMainLanguage(lastSearch.mainLanguageId)
        chooseLanguageRepository.setCurrentTranslationLanguage(lastSearch.translationLanguageId)
        searchedWord.postValue(lastSearch.text)
    }

    override fun onStarClick(historyItem: HistoryItem) {
        historyItem.saved = !historyItem.saved
        lastSearchRepository.updateLastSearchSaved(historyItem)
        historyItems.value = lastSearchRepository.getHistoryItems()
    }
}