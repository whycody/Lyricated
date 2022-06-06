package com.whycody.wordslife.search.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.*
import com.whycody.wordslife.search.SearchInteractor
import com.whycody.wordslife.search.mapper.LyricItemMapper

class SearchResultViewModel(private val lyricItemMapper: LyricItemMapper):
    ViewModel(), SearchInteractor {

    private val lyricItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searching = MutableLiveData(false)
    var typeOfLyrics = MutableLiveData(SearchResultFragment.MAIN_LYRICS)
    var searchWord = MutableLiveData("")

    private var currentShowedLyrics = 1
    private val numberOfShowingLyrics = 5
    private var allLyricItems = emptyList<LyricItem>()

    fun getLyricItems() = lyricItems

    fun submitLyricItems(findLyricsResponse: FindLyricsResponse) {
        val results = getResults(findLyricsResponse)
        allLyricItems =
            if(results.isNotEmpty()) results.map {
                lyricItemMapper.getLyricItemFromLyric(it, findLyricsResponse)
            } else emptyList()
        currentShowedLyrics = 1
        searching.postValue(false)
        postNewValues()
    }

    private fun getResults(findLyricsResponse: FindLyricsResponse) =
        if(typeOfLyrics.value == SearchResultFragment.MAIN_LYRICS) findLyricsResponse.mainResults
        else findLyricsResponse.similarResults

    fun setResultsReady(ready: Boolean) {
        if(!ready) searching.postValue(true)
    }

    override fun mainResultsHeaderClicked() {
        resultsHidden.value = !resultsHidden.value!!
        if(resultsHidden.value!!) {
            currentShowedLyrics = 1
            lyricItems.postValue(allLyricItems.take(currentShowedLyrics))
        } else collapseLyricItemsList()
    }

    private fun collapseLyricItemsList() = postNewValues(true)

    override fun showMoreResultsClicked() = postNewValues(false)

    fun setTypeOfLyrics(typeOfLyrics: String) = this.typeOfLyrics.postValue(typeOfLyrics)

    fun searchWord(word: String) {
        val formattedWord = getFormattedWord(word)
        if(formattedWord == searchWord.value) return
        searchWord.value = word
    }

    private fun getFormattedWord(word: String?) =
        word?.trim()?.replace(Regex("[+×÷=/_€£¥₩#\$%^*.?`~<>{}\\[\\]°•○●□■♤♡◇♧☆▪︎¤《》¡¿\"]"), "")

    private fun postNewValues(newNumber: Boolean = false) {
        val sizeOfShowedLyrics = updateNumberOfShowedLyricItems(newNumber)
        lyricItems.postValue(allLyricItems.take(sizeOfShowedLyrics))
        if(!newNumber) resultsAvailable.postValue(allLyricItems.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics < allLyricItems.size)
        searching.postValue(false)
    }

    private fun updateNumberOfShowedLyricItems(newNumber: Boolean): Int {
        if(resultsHidden.value!!) return currentShowedLyrics
        if(newNumber) currentShowedLyrics = 1
        if(shouldShowMoreResults()) currentShowedLyrics += numberOfShowingLyrics
        currentShowedLyrics += numberOfShowingLyrics
        return currentShowedLyrics
    }

    private fun shouldShowMoreResults() =
        allLyricItems.size.minus(currentShowedLyrics) < numberOfShowingLyrics * 2
}