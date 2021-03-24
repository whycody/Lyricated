package com.whycody.wordslife.data.last.searches

import com.whycody.wordslife.data.LastSearch

class LastSearchRepository(private val lastSearchDao: LastSearchDao) {

    private fun getAllLastSearches() = lastSearchDao.getAllLastSearches()

    fun flowFourLastSearches() = lastSearchDao.flowFourLastSearches()

    fun updateLastSearchSaved(id: Int, saved: Boolean) = lastSearchDao.updateLastSearchSaved(saved, id)

    fun refreshTime(time: Long, id: Int) = lastSearchDao.refreshTime(time, id)

    fun insertLastSearch(lastSearch: LastSearch) = lastSearchDao.insertLastSearch(lastSearch)

    fun getLastSearchById(id: Int) = lastSearchDao.getLastSearchById(id)
}