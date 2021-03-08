package com.whycody.wordslife.searchfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class SearchViewModel(private val lastSearchRepository: LastSearchRepository): ViewModel() {

    private val historyItems = MutableLiveData<List<HistoryItem>>()

    init {
        historyItems.postValue(lastSearchRepository.getHistoryItems())
    }

    fun insertHistoryItem(lastSearch: LastSearch) {
        lastSearchRepository.insertLastSearch(lastSearch)
        historyItems.postValue(lastSearchRepository.getHistoryItems())
    }

    fun getHistoryItems() = historyItems
}