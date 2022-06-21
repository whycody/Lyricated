package com.whycody.lyricated.data.last.searches

import com.whycody.lyricated.data.LastSearch
import java.util.*

class LastSearchRepository(private val lastSearchDao: LastSearchDao) {

    fun getAllLastSearches() = lastSearchDao.getAllLastSearches()

    fun getPagedLastSearches(limit: Int, offset: Int) = lastSearchDao.getPagedLastSearches(limit, offset)

    fun getPagedSavedLastSearches(limit: Int, offset: Int) = lastSearchDao.getPagedSavedLastSearches(limit, offset)

    fun flowFourLastSearches() = lastSearchDao.flowFourLastSearches()

    fun flowAllLastSearches() = lastSearchDao.flowAllLastSearches()

    fun flowAllSavedLastSearches() = lastSearchDao.flowAllSavedLastSearches()

    fun updateLastSearchSaved(id: Int, saved: Boolean) = lastSearchDao.updateLastSearchSaved(saved, id)

    fun refreshTime(id: Int) = lastSearchDao.refreshTime(Calendar.getInstance().timeInMillis, id)

    fun insertLastSearch(lastSearch: LastSearch) = lastSearchDao.insertLastSearch(lastSearch)

    fun getLastSearchById(id: Int) = lastSearchDao.getLastSearchById(id)

    fun deleteAllLastSearches() = lastSearchDao.deleteAllLastSearches()

    fun deselectAllLastSearches() = lastSearchDao.deselectAllLastSearches()
}