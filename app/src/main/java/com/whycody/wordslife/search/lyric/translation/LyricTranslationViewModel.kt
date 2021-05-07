package com.whycody.wordslife.search.lyric.translation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.TranslationItem
import com.whycody.wordslife.data.language.LanguageDao

class LyricTranslationViewModel(private val languageDao: LanguageDao): ViewModel() {

    private val translationItem = MutableLiveData<TranslationItem>()

    fun getTranslationItem() = translationItem

    fun findTranslation(extendedLyricItem: ExtendedLyricItem, typeOfPhrase: String) =
        if(typeOfPhrase == LyricTranslationFragment.MAIN_PHRASE)
            translationItem.postValue(getMainItemFromLyric(extendedLyricItem))
        else translationItem.postValue(getTranslationItemFromLyric(extendedLyricItem))

    private fun getMainItemFromLyric(extendedLyricItem: ExtendedLyricItem) =
        TranslationItem(
            languageDao.getLanguage(extendedLyricItem.languages.mainLangId)!!.drawable,
            extendedLyricItem.mainLangSentence)

    private fun getTranslationItemFromLyric(extendedLyricItem: ExtendedLyricItem) =
        TranslationItem(
            languageDao.getLanguage(extendedLyricItem.languages.translationLangId)!!.drawable,
            extendedLyricItem.translatedSentence)
}