package com.whycody.wordslife.library.studymode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.GetRandomLyricBody
import com.whycody.wordslife.data.api.ApiService
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.search.lyric.vocabulary.VocabularyInteractor
import com.whycody.wordslife.search.mapper.LyricItemMapper
import kotlinx.coroutines.launch
import kotlin.random.Random

class StudyModeViewModel(private val searchConfigurationDao: SearchConfigurationDao,
    private val apiService: ApiService, private val lyricItemMapper: LyricItemMapper): ViewModel(),
    VocabularyInteractor{

    private val extendedLyricItem = MutableLiveData<ExtendedLyricItem?>(null)
    private val numberOfAvailableWords = MutableLiveData(1)
    private val numberOfShownWords = MutableLiveData(0)
    private val shownWords = MutableLiveData(emptyList<Int>())
    private val loadingNextLyricItem = MutableLiveData(true)

    fun getExtendedLyricItem() = extendedLyricItem

    fun getNumberOfAvailableWords() = numberOfAvailableWords

    fun getNumberOfShownWords() = numberOfShownWords

    fun getShownWords() = shownWords

    fun getLoadingNextLyricItem() = loadingNextLyricItem

    fun showNextLyricItem() {
        tryGetRandomLyricFromApi()
        numberOfShownWords.postValue(0)
        shownWords.postValue(emptyList())
        loadingNextLyricItem.postValue(false)
    }

    fun nextBtnClicked() {
        if(numberOfAvailableWords.value==numberOfShownWords.value) {
            extendedLyricItem.postValue(null)
            loadingNextLyricItem.postValue(true)
        } else showAllWords()
    }

    private fun showAllWords() {
        numberOfShownWords.postValue(numberOfAvailableWords.value!!)
        this.shownWords.postValue((0 until numberOfAvailableWords.value!!).toList())
    }

    fun revealWordBtnClicked() {
        if(numberOfAvailableWords.value!!>numberOfShownWords.value!!) {
            numberOfShownWords.postValue(numberOfShownWords.value!!+1)
            addShownWordToList()
        }
    }

    private fun addShownWordToList() {
        val randomWordIndex = Random.nextInt(0, numberOfAvailableWords.value!!)
        if(shownWords.value!!.contains(randomWordIndex)) addShownWordToList()
        else shownWords.postValue(shownWords.value!!.plus(randomWordIndex))
    }

    private fun tryGetRandomLyricFromApi() {
        viewModelScope.launch {
            try { sendInquiryToApi()
            } catch (e: Exception) { }
        }
    }

    private suspend fun sendInquiryToApi() {
        val currentSearchConf = searchConfigurationDao.getSearchConfiguration()
        val getRandomLyricBody = GetRandomLyricBody(24, currentSearchConf.lyricLanguages.mainLangId,
            currentSearchConf.lyricLanguages.translationLangId)
        val response = apiService.getRandomLyric(getRandomLyricBody)
        extendedLyricItem.value = lyricItemMapper.getExtendedLyricItemFromLyric(response.body()!!)
        numberOfAvailableWords.postValue(getNumberOfWords())
    }

    private fun getNumberOfWords() = extendedLyricItem.value!!.mainLangSentence?.trim()?.
        split(" ")?.filter { it.any { character -> character.isLetter() } }?.size

    override fun wordClicked(index: Int, word: String) {
        if(shownWords.value!!.contains(index)) return
        numberOfShownWords.postValue(numberOfShownWords.value!!+1)
        shownWords.postValue(shownWords.value!!.plus(index))
    }
}