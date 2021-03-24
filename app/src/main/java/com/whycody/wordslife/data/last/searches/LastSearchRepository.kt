package com.whycody.wordslife.data.last.searches

import com.whycody.wordslife.data.HistoryItem
import com.whycody.wordslife.data.LastSearch
import com.whycody.wordslife.data.language.LanguageDao

class LastSearchRepository(private val lastSearchDao: LastSearchDao,
    private val languageDao: LanguageDao) {

    private fun getAllLastSearches() = lastSearchDao.getAllLastSearches()

    private fun getFourLastSearches() = lastSearchDao.getFourLastSearches()

    fun updateLastSearchSaved(id: Int, saved: Boolean) = lastSearchDao.updateLastSearchSaved(saved, id)

    fun refreshTime(time: Long, id: Int) = lastSearchDao.refreshTime(time, id)

    fun insertLastSearch(word: String) = lastSearchDao.insertLastSearch(getCurrentLastSearchItem(word))

    private fun getCurrentLastSearchItem(word: String) = LastSearch(
            mainLanguageId = languageDao.getCurrentMainLanguage().id,
            translationLanguageId = languageDao.getCurrentTranslationLanguage().id,
            text = word)

    fun getLastSearchById(id: Int) = lastSearchDao.getLastSearchById(id)

    fun getHistoryItems() = getFourLastSearches().map {
       getHistoryItemFromLastSearch(it)}

    private fun getHistoryItemFromLastSearch(lastSearch: LastSearch) =
        HistoryItem(lastSearch.id, lastSearch.text,
                languageDao.getLanguage(lastSearch.mainLanguageId)!!.drawable,
                languageDao.getLanguage(lastSearch.translationLanguageId)!!.drawable,
                lastSearch.saved)

}