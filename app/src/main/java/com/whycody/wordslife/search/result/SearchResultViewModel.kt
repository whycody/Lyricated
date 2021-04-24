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
    private val allLyricItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searching = MutableLiveData(false)
    var typeOfLyrics = MutableLiveData(SearchResultFragment.MAIN_LYRICS)
    var searchWord = MutableLiveData<String>()
    private val numberOfShowingLyrics = 5

    private var currentShowedLyrics = MutableStateFlow(numberOfShowingLyrics)
    private val searchWordFlow = MutableStateFlow("")
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages())

    fun getLyricItems() = lyricItems

    suspend fun collectLyricItems() = flowLyricItems().collectLatest { lyricItems ->
        updateNumberOfShowingLyrics(lyricItems, true)
        this.lyricItems.postValue(lyricItems.take(currentShowedLyrics.value))
        allLyricItems.postValue(lyricItems)
        postNewValues(lyricItems)
    }

    private fun flowLyricItems(): Flow<List<LyricItem>> = searchWordFlow.combine(lyricLanguagesFlow) { word, languages ->
        if(word.length == 1 && typeOfLyrics.value == SearchResultFragment.SIMILAR_LYRICS) emptyList<LyricItem>()
        searching.postValue(true)
        getLyricItemsList(word, languages)
    }

    private fun getLyricItemsList(word: String, languages: LyricLanguages, queryLimit: Boolean = true): List<LyricItem> {
        val regex = Regex(getPattern(typeOfLyrics.value!!, word), RegexOption.IGNORE_CASE)
        val lyricItems = lyricsRepository.getLyricItemsWithWord(getSearchingWord(word), languages, queryLimit)
        val filteredLyricItems = lyricItems.filter { isLyricItemRight(word, regex, it) }
        return if(filteredLyricItems.size > 20 || lyricItems.size < LyricsQueryBuilderImpl.DEFAULT_QUERY_LIMIT || !queryLimit)
            filteredLyricItems
        else getLyricItemsList(word, languages, false)
    }

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
            currentShowedLyrics.value = 1
            lyricItems.postValue(allLyricItems.value!!.take(currentShowedLyrics.value))
        } else collapseLyricItemsList()
    }

    private fun collapseLyricItemsList() = refreshLyricItems(true)

    override fun showMoreResultsClicked() = refreshLyricItems()

    private fun refreshLyricItems(newNumber: Boolean = false) {
        if(allLyricItems.value == null) return
        updateNumberOfShowingLyrics(newNumber = newNumber)
        lyricItems.postValue(allLyricItems.value!!.take(currentShowedLyrics.value))
        postNewValues()
    }

    fun setTypeOfLyrics(typeOfLyrics: String) {
        this.typeOfLyrics.value = typeOfLyrics
    }

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = this.lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun searchWord(word: String) {
        val formattedWord = getFormattedWord(word)
        if(formattedWord == searchWord.value) return
        searchWordFlow.tryEmit(formattedWord!!)
        searchWord.value = word
    }

    private fun getFormattedWord(word: String?) = word?.trim()?.replace(Regex("[*.?]"), "")

    private fun postNewValues(lyrics: List<LyricItem>? = null) {
        resultsAvailable.postValue(lyrics?.isNotEmpty() ?: lyricItems.value?.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics.value < lyrics?.size ?: allLyricItems.value!!.size)
        searching.postValue(false)
    }

    private fun updateNumberOfShowingLyrics(lyrics: List<LyricItem>? = null, newNumber: Boolean = false) {
        if(resultsHidden.value!!) return
        if(newNumber) currentShowedLyrics.value = 0
        if(getDifference(lyrics) < numberOfShowingLyrics * 2)
            currentShowedLyrics.value += numberOfShowingLyrics
        if(currentShowedLyrics.value == 0) currentShowedLyrics.value += 1
        currentShowedLyrics.value += numberOfShowingLyrics
    }

    private fun getDifference(lyrics: List<LyricItem>?) =
            lyrics?.size?.minus(currentShowedLyrics.value)
            ?: allLyricItems.value!!.size.minus(currentShowedLyrics.value)

}