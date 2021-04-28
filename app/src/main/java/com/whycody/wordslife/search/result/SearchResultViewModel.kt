package com.whycody.wordslife.search.result

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.lyrics.LyricsQueryBuilderImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.search.SearchInteractor
import kotlinx.coroutines.flow.*

class SearchResultViewModel(private val lyricsRepository: LyricsRepository): ViewModel(), SearchInteractor {

    private val lyricItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searching = MutableLiveData(false)
    var typeOfLyrics = MutableLiveData(SearchResultFragment.MAIN_LYRICS)
    var searchWord = MutableLiveData("")
    private val mainResultsReady = MutableLiveData(false)
    private val similarResultsReady = MutableLiveData(false)

    private var currentShowedLyrics = 1
    private val numberOfShowingLyrics = 5
    private var allLyricItems = emptyList<LyricItem>()
    private val searchWordFlow = MutableStateFlow("")
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages())

    fun getLyricItems() = lyricItems

    fun getMainResultsReady() = mainResultsReady

    fun getSimilarResultsReady() = similarResultsReady

    suspend fun collectLyricItems() = flowLyricItems().collectLatest { lyricItems ->
        allLyricItems = lyricItems
        currentShowedLyrics = 1
        setResultsReady(true)
    }

    private fun flowLyricItems(): Flow<List<LyricItem>> =
            searchWordFlow.combine(lyricLanguagesFlow) { word, languages ->
                if(wordIsNotCorrect(word)) emptyList<LyricItem>()
                searching.postValue(true)
                setResultsReady(false)
                getLyricItemsList(word, languages)
            }

    private fun setResultsReady(ready: Boolean) {
        if(typeOfLyrics.value == SearchResultFragment.MAIN_LYRICS) mainResultsReady.postValue(ready)
        else similarResultsReady.postValue(ready)
    }

    private fun wordIsNotCorrect(word: String) =
            word.length == 1 && typeOfLyrics.value == SearchResultFragment.SIMILAR_LYRICS

    private fun getLyricItemsList(word: String, languages: LyricLanguages,
                                  queryLimit: Boolean = true): List<LyricItem> {
        val regex = Regex(getPattern(typeOfLyrics.value!!, word), RegexOption.IGNORE_CASE)
        val lyricItems = lyricsRepository.getLyricItemsWithWord(getSearchingWord(word), languages, queryLimit)
        val filteredLyricItems = lyricItems.filter { isLyricItemRight(word, regex, it) }.take(60)
        return if(shouldNotSearchMore(filteredLyricItems, lyricItems, queryLimit)) filteredLyricItems
        else getLyricItemsList(word, languages, false)
    }

    private fun shouldNotSearchMore(newList: List<LyricItem>, currentList: List<LyricItem>, queryLimit: Boolean) =
            newList.size >= 10 || currentList.size < LyricsQueryBuilderImpl.DEFAULT_QUERY_LIMIT || !queryLimit

    private fun getSearchingWord(word: String) =
            if(typeOfLyrics.value == SearchResultFragment.MAIN_LYRICS || word.length < 4) word
            else word.substring(0, word.length-2)

    private fun isLyricItemRight(word: String, regex: Regex, lyricItem: LyricItem): Boolean {
        val foundWord = regex.find(lyricItem.mainSentence) ?: return false
        if(typeOfLyrics.value == SearchResultFragment.SIMILAR_LYRICS &&
                foundWord.value.length >= word.length*2) return false
        setSentenceSpan(regex, lyricItem)
        return true
    }

    private fun setSentenceSpan(regex: Regex, lyric: LyricItem) {
        val stb = SpannableStringBuilder(lyric.mainSentence)
        regex.findAll(lyric.mainSentence).forEach { stb.setSpan(StyleSpan(Typeface.BOLD),
                it.range.first, it.range.last+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
        lyric.mainSentenceSpan = stb
    }

    private fun getPattern(typeOfLyrics: String, word: String) =
            if(typeOfLyrics == SearchResultFragment.MAIN_LYRICS) getMainLyricsPattern(word)
            else getSimilarLyricsPattern(word)

    private fun getMainLyricsPattern(word: String) = "\\b$word\\b[^']"

    private fun getSimilarLyricsPattern(word: String) =
            when {
                word.length > 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,?! ]\\S*|\\b${word.substring(0, word.length-1)}\\b"
                word.length == 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,?! ]\\S*"
                else -> "\\b\\S$word\\S?[^\\s]*|\\b\\S?$word[^.,?! ][^\\s]*"
            }

    override fun mainResultsHeaderClicked() {
        resultsHidden.value = !resultsHidden.value!!
        if(resultsHidden.value!!) {
            currentShowedLyrics = 1
            lyricItems.postValue(allLyricItems.take(currentShowedLyrics))
        } else collapseLyricItemsList()
    }

    private fun collapseLyricItemsList() = refreshLyricItems(true)

    override fun showMoreResultsClicked() = refreshLyricItems(false)

    private fun refreshLyricItems(newNumber: Boolean) {
        if(newNumber) currentShowedLyrics = 1
        postNewValues()
    }

    fun setTypeOfLyrics(typeOfLyrics: String) = this.typeOfLyrics.postValue(typeOfLyrics)

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun searchWord(word: String) {
        val formattedWord = getFormattedWord(word)
        if(formattedWord == searchWord.value) return
        searchWordFlow.tryEmit(formattedWord!!)
        searchWord.value = word
    }

    private fun getFormattedWord(word: String?) =
            word?.trim()?.replace(Regex("[*.?]"), "")

    fun postNewValues() {
        val sizeOfShowedLyrics = updateNumberOfShowedLyricItems()
        lyricItems.postValue(allLyricItems.take(sizeOfShowedLyrics))
        resultsAvailable.postValue(allLyricItems.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics < allLyricItems.size)
        searching.postValue(false)
    }

    private fun updateNumberOfShowedLyricItems(): Int {
        if(resultsHidden.value!!) return currentShowedLyrics
        if(getDifference(allLyricItems) < numberOfShowingLyrics * 2)
            currentShowedLyrics += numberOfShowingLyrics
        currentShowedLyrics += numberOfShowingLyrics
        return currentShowedLyrics
    }

    private fun getDifference(lyricItems: List<LyricItem>) =
            lyricItems.size.minus(currentShowedLyrics)

}