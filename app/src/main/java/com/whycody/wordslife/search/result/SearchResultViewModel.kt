package com.whycody.wordslife.search.result

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.Lyric
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import com.whycody.wordslife.search.SearchInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchResultViewModel(private val lyricsRepository: LyricsRepository,
                            languageDao: LanguageDao): ViewModel(), SearchInteractor {

    private val lyricItems = MutableLiveData<List<LyricItem>>()
    private val allLyricItems = MutableLiveData<List<LyricItem>>()
    var resultsAvailable = MutableLiveData(true)
    var thereAreMoreResults = MutableLiveData(true)
    var resultsHidden = MutableLiveData(false)
    var searching = MutableLiveData(false)
    var typeOfLyrics = MutableLiveData(SearchResultFragment.MAIN_LYRICS)
    var searchWord = MutableLiveData("_")
    private val numberOfShowingLyrics = 5

    private var currentShowedLyrics = MutableStateFlow(numberOfShowingLyrics)
    private val searchWordFlow = MutableStateFlow("")
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages(
            languageDao.getCurrentMainLanguage().id, languageDao.getCurrentTranslationLanguage().id))

    fun getLyricItems() = lyricItems

    init {
        MainScope().launch(Dispatchers.IO) {
            collectLyricItems()
        }
    }

    private suspend fun collectLyricItems() = flowLyricItems().collectLatest { lyricItems ->
        allLyricItems.postValue(lyricItems)
        this.lyricItems.postValue(lyricItems.take(currentShowedLyrics.value))
        postNewValues(lyricItems)
        searching.postValue(false)
    }

    private fun flowLyricItems(): Flow<List<LyricItem>> = searchWordFlow.combine(lyricLanguagesFlow) { word, lyricLanguages ->
        searching.postValue(true)
        val regex = Regex(getPattern(typeOfLyrics.value!!, word), RegexOption.IGNORE_CASE)
        val lyricsList = lyricsRepository.getLyricsWithWordIncludedInLanguage(lyricLanguages.mainLanguageId, getSearchingWord(word))
        currentShowedLyrics.value = 0
        updateNumberOfShowingLyrics(lyricsList)
        lyricsList.filter { isLyricRight(word, regex, it) }
                .map { getLyricItemFromLyric(it) }
                .distinctBy { it.mainLangSentence.toString().toLowerCase() }
    }

    private fun getSearchingWord(word: String) =
            if(typeOfLyrics.value == SearchResultFragment.MAIN_LYRICS || word.length < 4) word
            else word.substring(0, word.length-2)

    private fun isLyricRight(word: String, regex: Regex, lyric: Lyric): Boolean {
        if(getTranslatedSentence(lyric) == null) return false
        val foundWord = regex.find(getMainSentence(lyric))
        return if(foundWord == null) false
        else foundWord.value.length <= word.length*2
    }

    private fun getLyricItemFromLyric(lyric: Lyric) =
            LyricItem(lyric.lyricId, getSentenceSpan(getMainSentence(lyric), searchWordFlow.value,
                    typeOfLyrics.value!!), getTranslatedSentence(lyric)!!)

    private fun getMainSentence(lyric: Lyric) =
            getSentenceFromLang(lyricLanguagesFlow.value.mainLanguageId, lyric)!!

    private fun getTranslatedSentence(lyric: Lyric) =
            getSentenceFromLang(lyricLanguagesFlow.value.translationLanguageId, lyric)

    private fun getSentenceFromLang(langId: String, lyric: Lyric) =
            when(langId) {
                LanguageDaoImpl.PL -> lyric.pl
                LanguageDaoImpl.ENG -> lyric.eng
                LanguageDaoImpl.PT -> lyric.pt
                LanguageDaoImpl.GER -> lyric.ger
                LanguageDaoImpl.FR -> lyric.fr
                LanguageDaoImpl.ESP -> lyric.esp
                else -> lyric.it
            }

    private fun getSentenceSpan(mainSentence: String, word: String, typeOfLyrics: String): SpannableStringBuilder {
        val stb = SpannableStringBuilder(mainSentence)
        val regex = Regex(getPattern(typeOfLyrics, word), RegexOption.IGNORE_CASE)
        regex.findAll(mainSentence).forEach{
            stb.setSpan(StyleSpan(Typeface.BOLD),
                    it.range.first, it.range.last+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return stb
    }

    private fun getPattern(typeOfLyrics: String, word: String) =
            if(typeOfLyrics == SearchResultFragment.MAIN_LYRICS) getMainLyricsPattern(word)
            else getSimilarLyricsPattern(word)

    private fun getMainLyricsPattern(word: String) = "\\b$word\\b"

    private fun getSimilarLyricsPattern(word: String) =
            when {
                word.length > 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,? ]\\S*|\\b${word.substring(0, word.length-1)}\\b"
                word.length == 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,? ]\\S*"
                else -> "\\b\\S$word\\S?[^\\s]*|\\b\\S?$word[^.,? ][^\\s]*"
            }

    override fun mainResultsHeaderClicked() {
        resultsHidden.postValue(!resultsHidden.value!!)
        if(!resultsHidden.value!!) collapseLyricItemsList()
    }

    private fun collapseLyricItemsList() {
        currentShowedLyrics.value = 0
        refreshLyricItems()
    }

    override fun showMoreResultsClicked() = refreshLyricItems()

    private fun refreshLyricItems() {
        if(allLyricItems.value == null) return
        updateNumberOfShowingLyrics()
        lyricItems.postValue(allLyricItems.value!!.take(currentShowedLyrics.value))
        postNewValues()
    }

    fun setTypeOfLyrics(typeOfLyrics: String) {
        this.typeOfLyrics.value = typeOfLyrics
    }

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = this.lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun searchWord(word: String) {
        emitWordIfIsCorrect(getFormattedWord(word))
        searchWord.value = word
    }

    private fun getFormattedWord(word: String) = word.trim().replace(Regex("[*.?]"), "")

    private fun emitWordIfIsCorrect(word: String) {
        if(word.length <= 1 && typeOfLyrics.value == SearchResultFragment.SIMILAR_LYRICS)
            resultsAvailable.postValue(false)
        else searchWordFlow.tryEmit(word)
    }

    private fun postNewValues(lyrics: List<LyricItem>? = null) {
        resultsAvailable.postValue(lyrics?.isNotEmpty() ?: lyricItems.value?.isNotEmpty())
        thereAreMoreResults.postValue(currentShowedLyrics.value < lyrics?.size ?: allLyricItems.value!!.size)
    }

    private fun updateNumberOfShowingLyrics(lyrics: List<Lyric>? = null) {
        val difference = lyrics?.size?.minus(currentShowedLyrics.value)
                ?: allLyricItems.value!!.size.minus(currentShowedLyrics.value)
        currentShowedLyrics.value =
                if(difference < numberOfShowingLyrics*2) numberOfShowingLyrics + difference
                else numberOfShowingLyrics + currentShowedLyrics.value
    }

}