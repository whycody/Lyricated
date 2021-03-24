package com.whycody.wordslife.search.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.search.SearchInteractor

class SearchResultViewModel(private val lyricsRepository: LyricsRepository,
                            private val languageDao: LanguageDao): ViewModel(), SearchInteractor {

    private val lyricItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searchWord = MutableLiveData("")
    var typeOfLyrics = MutableLiveData(SearchResultFragment.MAIN_LYRICS)
    private val numberOfShowingLyrics = 5
    private var currentShowedLyrics = numberOfShowingLyrics

    fun getLyricItems() = lyricItems

    override fun mainResultsHeaderClicked() {
        resultsHidden.postValue(!resultsHidden.value!!)
        if(!resultsHidden.value!!) collapseLyricItemsList()
    }

    private fun collapseLyricItemsList() {
        currentShowedLyrics = 0
        updateNumberOfShowingLyrics(lyricItems.value!!)
        lyricItems.postValue(lyricItems.value!!.take(currentShowedLyrics))
    }

    override fun showMoreResultsClicked() = updateLyricsItems(searchWord.value!!)

    fun searchWord(word: String, typeOfLyrics: String) {
        this.typeOfLyrics.value = typeOfLyrics
        currentShowedLyrics = 0
        updateLyricsItems(word)
    }

    private fun updateLyricsItems(word: String) {
        var newLyricItems = getLyricItems(word)
        updateNumberOfShowingLyrics(newLyricItems)
        newLyricItems = newLyricItems.take(currentShowedLyrics)
        postNewValues(word, newLyricItems)
    }

    private fun postNewValues(word: String, newLyricItems: List<LyricItem>) {
        searchWord.postValue(word)
        lyricItems.postValue(newLyricItems)
        resultsAvailable.postValue(newLyricItems.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics <= newLyricItems.size)
    }

    private fun getLyricItems(word: String) =
            if(typeOfLyrics.value!! == SearchResultFragment.MAIN_LYRICS)
                lyricsRepository.getMainLyricItemsWithWordIncluded(
                        languageDao.getCurrentMainLanguage().id,
                        languageDao.getCurrentTranslationLanguage().id,
                        word)
            else lyricsRepository.getSimilarLyricItemsWithWordIncluded(
                    languageDao.getCurrentMainLanguage().id,
                    languageDao.getCurrentTranslationLanguage().id,
                    word)

    private fun updateNumberOfShowingLyrics(lyricItems: List<LyricItem>) {
        val difference = lyricItems.size.minus(currentShowedLyrics)
        currentShowedLyrics +=
                if(difference < numberOfShowingLyrics*2) numberOfShowingLyrics*2
                else numberOfShowingLyrics
    }

}