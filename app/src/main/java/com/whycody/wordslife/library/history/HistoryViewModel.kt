package com.whycody.wordslife.library.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.last.searches.LastSearchRepository

class HistoryViewModel(private val lastSearchRepository: LastSearchRepository,
                       private val languageDao: LanguageDao): ViewModel() {

    fun getData(onlySaved: Boolean) = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            initialLoadSize = 15
        ),
    ) {
        HistoryPagingSource(lastSearchRepository, languageDao, onlySaved)
    }.liveData.cachedIn(viewModelScope)
}