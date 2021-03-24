package com.whycody.wordslife.search.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.search.SearchInteractor

class SearchResultViewModel(private val lyricsRepository: LyricsRepository,
                            private val languageDao: LanguageDao): ViewModel(), SearchInteractor {

    private val lyricsItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searchWord = MutableLiveData("")
    var typeOfLyrics = MutableLiveData(SearchResultFragment.MAIN_LYRICS)
    private val numberOfShowingLyrics = 5
    private var currentShowedLyrics = numberOfShowingLyrics

    fun getLyricsItems() = lyricsItems

    override fun mainResultsHeaderClicked() {
        resultsHidden.postValue(!resultsHidden.value!!)
        if(!resultsHidden.value!!)
            lyricsItems.postValue(lyricsItems.value!!.take(numberOfShowingLyrics))
    }

    override fun showMoreResultsClicked() {
        currentShowedLyrics += numberOfShowingLyrics
        updateLyricsItems(searchWord.value!!)
    }

    fun searchWord(word: String, typeOfLyrics: String) {
        this.typeOfLyrics.value = typeOfLyrics
        currentShowedLyrics = numberOfShowingLyrics
        updateLyricsItems(word)
    }

    private fun updateLyricsItems(word: String) {
        val newLyricsItems = getLyricsItems(word)
        searchWord.postValue(word)
        lyricsItems.postValue(newLyricsItems)
        resultsAvailable.postValue(newLyricsItems.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics <= newLyricsItems.size)
    }

    private fun getLyricsItems(word: String) =
            if(typeOfLyrics.value!! == SearchResultFragment.MAIN_LYRICS)
                lyricsRepository.getMainLyricItemsWithWordIncluded(
                        languageDao.getCurrentMainLanguage().id,
                        languageDao.getCurrentTranslationLanguage().id,
                        word, currentShowedLyrics)
            else lyricsRepository.getSimilarLyricItemsWithWordIncluded(
                    languageDao.getCurrentMainLanguage().id,
                    languageDao.getCurrentTranslationLanguage().id,
                    word, currentShowedLyrics)

}