package com.whycody.wordslife.library.history

import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class HistoryViewModel(private val lastSearchRepository: LastSearchRepository): ViewModel() {

    fun cleanListBtnClicked(onlySaved: Boolean) {
        if(onlySaved) lastSearchRepository.deselectAllLastSearches()
        else lastSearchRepository.deleteAllLastSearches()
    }
}