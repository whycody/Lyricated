package com.whycody.wordslife.search.lyric.translation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.TranslationItem
import com.whycody.wordslife.data.language.LanguageDao

class TranslationViewModel(private val languageDao: LanguageDao): ViewModel() {

    private val translationItem = MutableLiveData<TranslationItem>()

    fun getTranslationItem() = translationItem

    fun findTranslation(extendedLyricItem: ExtendedLyricItem) =
        translationItem.postValue(getTranslationItemFromLyric(extendedLyricItem))

    private fun getTranslationItemFromLyric(extendedLyricItem: ExtendedLyricItem) =
        TranslationItem(
            languageDao.getLanguage(extendedLyricItem.languages.translationLanguageId)!!.drawable,
            extendedLyricItem.translatedSentence)
}