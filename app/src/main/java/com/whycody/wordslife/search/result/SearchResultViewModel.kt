package com.whycody.wordslife.search.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.lyrics.LyricsQueryBuilderImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.search.SearchInteractor
import com.whycody.wordslife.search.result.span.builder.SearchResultSpanBuilder
import kotlinx.coroutines.flow.*

class SearchResultViewModel(private val lyricsRepository: LyricsRepository,
                            private val spanBuilder: SearchResultSpanBuilder): ViewModel(), SearchInteractor {

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
    private val translationsFlow = MutableStateFlow(emptyList<Translation>())

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
                if(!wordIsNotCorrect(word)) {
                    setResultsReady(false)
                    getLyricItemsList(word, languages)
                } else emptyList()
            }

    private fun setResultsReady(ready: Boolean) {
        if(!ready) searching.postValue(true)
        if(typeOfLyrics.value == SearchResultFragment.MAIN_LYRICS) mainResultsReady.postValue(ready)
        else similarResultsReady.postValue(ready)
    }

    private fun wordIsNotCorrect(word: String) =
            word.length == 1 && typeOfLyrics.value == SearchResultFragment.SIMILAR_LYRICS

    private fun getLyricItemsList(word: String, languages: LyricLanguages,
                                  queryLimit: Boolean = true): List<LyricItem> {
        val regex = Regex(getMainSentencePattern(typeOfLyrics.value!!, word), RegexOption.IGNORE_CASE)
        val lyricItems = lyricsRepository.getLyricItemsWithWord(getSearchingWord(word), languages, queryLimit)
        val filteredLyricItems = lyricItems.filter { isLyricItemRight(word, regex, it) }.take(100)
        return if(shouldNotSearchMore(filteredLyricItems, lyricItems, queryLimit)) filteredLyricItems
        else getLyricItemsList(word, languages, false)
    }

    private fun shouldNotSearchMore(newList: List<LyricItem>, currentList: List<LyricItem>, queryLimit: Boolean) =
            newList.size >= 10 || currentList.size < LyricsQueryBuilderImpl.DEFAULT_QUERY_LIMIT || !queryLimit

    private fun getSearchingWord(word: String) =
            if(typeOfLyrics.value == SearchResultFragment.MAIN_LYRICS || word.length < 4) word
            else word.substring(0, word.length - 2)

    private fun isLyricItemRight(word: String, regex: Regex, lyricItem: LyricItem): Boolean {
        val foundWord = regex.find(lyricItem.mainSentence) ?: return false
        if(foundWord.value.trim().length >= word.length*2 || lyricItemIsDoubled(word, lyricItem)) return false
        spanBuilder.setMainSentenceSpan(regex, lyricItem)
        return true
    }

    private fun lyricItemIsDoubled(word: String, lyricItem: LyricItem) =
            typeOfLyrics.value == SearchResultFragment.SIMILAR_LYRICS &&
                    Regex(getMainLyricsPattern(word), RegexOption.IGNORE_CASE)
                            .find(lyricItem.mainSentence) != null

    private fun getMainSentencePattern(typeOfLyrics: String, word: String) =
            if(typeOfLyrics == SearchResultFragment.MAIN_LYRICS) getMainLyricsPattern(word)
            else getSimilarLyricsPattern(word)

    private fun getMainLyricsPattern(word: String) = "\\b$word\\b[^']"

    private fun getSimilarLyricsPattern(word: String) =
            when {
                word.length > 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,?! ]\\S*|\\b${word.substring(0, word.length - 1)}\\b"
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

    private fun collapseLyricItemsList() = postNewValues(true)

    override fun showMoreResultsClicked() = postNewValues(false)

    fun setTypeOfLyrics(typeOfLyrics: String) = this.typeOfLyrics.postValue(typeOfLyrics)

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun applyTranslations(translations: List<Translation>) = translationsFlow.tryEmit(translations)

    fun searchWord(word: String) {
        val formattedWord = getFormattedWord(word)
        if(formattedWord == searchWord.value) return
        searchWordFlow.tryEmit(formattedWord!!)
        searchWord.value = word
    }

    private fun getFormattedWord(word: String?) =
            word?.trim()?.replace(Regex("[*.?]"), "")

    fun postNewValues(newNumber: Boolean = false) {
        val sizeOfShowedLyrics = updateNumberOfShowedLyricItems(newNumber)
        lyricItems.postValue(allLyricItems.take(sizeOfShowedLyrics))
        resultsAvailable.postValue(allLyricItems.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics < allLyricItems.size)
        searching.postValue(false)
    }

    fun setupTranslations() {
        allLyricItems = spanBuilder.getSortedLyricItemsWithTranslationSpan(allLyricItems, translationsFlow.value)
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