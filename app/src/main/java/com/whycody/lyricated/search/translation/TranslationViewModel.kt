package com.whycody.lyricated.search.translation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.Translation
import com.whycody.lyricated.data.search.configuration.SearchConfigurationDao

class TranslationViewModel(private val searchConfDao: SearchConfigurationDao): ViewModel() {

    private val loadingList = listOf(
            Translation(translatedPhrase = "          ", type = LOADING),
            Translation(translatedPhrase = "                ", type = LOADING),
            Translation(translatedPhrase = "           ", type = LOADING),
            Translation(translatedPhrase = "                         ", type = LOADING))
    private val loading = MutableLiveData(true)
    private val translations = MutableLiveData(loadingList)

    fun submitTranslations(translations: List<String>) {
        val currentTranslationLangId = searchConfDao.getLyricLanguages().translationLangId
        this.translations.postValue(translations.map { Translation(it,
            translationLangId = currentTranslationLangId)})
    }

    fun setResultsReady(findLyricsResultResponse: Boolean) {
        if(!findLyricsResultResponse) {
            loading.postValue(!findLyricsResultResponse)
            translations.postValue(loadingList)
        }
    }

    fun getTranslations() = translations

    fun getLoading() = loading

    companion object {
        const val LOADING = 0
        const val TRANSLATION = 1
    }
}