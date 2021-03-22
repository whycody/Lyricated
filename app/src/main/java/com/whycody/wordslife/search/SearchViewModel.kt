package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.lyrics.LyricsRepository

class SearchViewModel(private val lyricsRepository: LyricsRepository,
                      private val languageDao: LanguageDao): ViewModel(), SearchInteractor {

    private val lyricsItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searchWord = MutableLiveData("")
    private var numberOfLyrics = 10

    fun getLyricsItems() = lyricsItems

    override fun mainResultsHeaderClicked() = resultsHidden.postValue(!resultsHidden.value!!)

    override fun showMoreResultsClicked() {
        numberOfLyrics += 10
        updateLyricsItems(searchWord.value!!)
    }

    fun searchWord(word: String) {
        numberOfLyrics = 10
        updateLyricsItems(word)
    }

    private fun updateLyricsItems(word: String) {
        val newLyricsItems = lyricsRepository.getLyricItemsWithWordIncluded(
                languageDao.getCurrentMainLanguage().id,
                languageDao.getCurrentTranslationLanguage().id,
                word, numberOfLyrics)
        searchWord.postValue(word)
        lyricsItems.postValue(newLyricsItems)
        resultsAvailable.postValue(newLyricsItems.isNotEmpty())
        thereAreMoreResults.postValue(numberOfLyrics <= newLyricsItems.size)
    }
}