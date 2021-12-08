package com.whycody.wordslife.data.last.searches

import com.whycody.wordslife.data.LastSearch
import java.util.*

class LastSearchRepository(private val lastSearchDao: LastSearchDao) {

    fun getAllLastSearches() = lastSearchDao.getAllLastSearches()

    fun flowFourLastSearches() = lastSearchDao.flowFourLastSearches()

    fun flowAllLastSearches() = lastSearchDao.flowAllLastSearches()

    fun flowAllSavedLastSearches() = lastSearchDao.flowAllSavedLastSearches()

    fun updateLastSearchSaved(id: Int, saved: Boolean) = lastSearchDao.updateLastSearchSaved(saved, id)

    fun refreshTime(id: Int) = lastSearchDao.refreshTime(Calendar.getInstance().timeInMillis, id)

    fun insertLastSearch(lastSearch: LastSearch) = lastSearchDao.insertLastSearch(lastSearch)

    fun getLastSearchById(id: Int) = lastSearchDao.getLastSearchById(id)
}