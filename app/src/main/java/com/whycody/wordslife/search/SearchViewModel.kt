package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.lyrics.LyricsRepository

class SearchViewModel(private val lyricsRepository: LyricsRepository,
                      private val languageDao: LanguageDao): ViewModel(), SearchInteractor {

    private val lyricsItems = MutableLiveData<List<LyricItem>>()
    private var thereAreMoreResults = MutableLiveData<Boolean>()
    private var searchWord: String = ""
    private var numberOfLyrics = 30

    fun getLyricsItems() = lyricsItems

    fun thereAreMoreResults() = thereAreMoreResults

    override fun showMoreResults() {
        numberOfLyrics += 30
        updateLyricsItems(searchWord)
    }

    fun searchWord(word: String) {
        numberOfLyrics = 30
        updateLyricsItems(word)
    }

    private fun updateLyricsItems(word: String) {
        searchWord = word
        val newLyricsItems = lyricsRepository.getLyricItemsWithWordIncluded(
                languageDao.getCurrentMainLanguage().id,
                languageDao.getCurrentTranslationLanguage().id,
                word, numberOfLyrics)
        lyricsItems.postValue(newLyricsItems)
        thereAreMoreResults.postValue(numberOfLyrics <= newLyricsItems.size)
    }
}