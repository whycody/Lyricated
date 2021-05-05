package com.whycody.wordslife.search.translation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.Translation
import com.whycody.wordslife.data.translation.TranslationDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class TranslationViewModel(private val translationDao: TranslationDao): ViewModel() {

    private val loadingList = listOf(
            Translation(translatedPhrase = "          ", type = LOADING),
            Translation(translatedPhrase = "                ", type = LOADING),
            Translation(translatedPhrase = "           ", type = LOADING),
            Translation(translatedPhrase = "                         ", type = LOADING))
    private val loading = MutableLiveData(true)
    private val translations = MutableLiveData(loadingList)
    private val searchWordFlow = MutableStateFlow("")
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages())

    fun getTranslations() = translations

    fun getLoading() = loading

    suspend fun collectTranslations() = flowTranslations().collect {
        if(it.isNotEmpty()) translations.postValue(it)
        loading.postValue(false)
    }

    private fun flowTranslations(): Flow<List<Translation>> =
        searchWordFlow.combine(lyricLanguagesFlow) { word, _ ->
            loading.postValue(true)
            translations.postValue(loadingList)
            getTranslations(word)
        }

    private fun getTranslations(word: String) = translationDao.tryTranslate(word)?: emptyList()

    fun searchWord(word: String) = searchWordFlow.tryEmit(word)

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = lyricLanguagesFlow.tryEmit(lyricLanguages)

    companion object {
        const val LOADING = 0
        const val TRANSLATION = 1
    }
}