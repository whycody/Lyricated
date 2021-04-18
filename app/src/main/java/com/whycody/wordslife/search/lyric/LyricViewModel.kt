package com.whycody.wordslife.search.lyric

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class LyricViewModel(private val lyricsRepository: LyricsRepository, languageDao: LanguageDao): ViewModel() {

    private val currentExtendedLyricItem = MutableLiveData<ExtendedLyricItem>()
    private val lyricIdFlow = MutableStateFlow(0)
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages(
            languageDao.getCurrentMainLanguage().id, languageDao.getCurrentTranslationLanguage().id))

    suspend fun collectLyricItem() = flowExtendedLyricItem().collect { currentExtendedLyricItem.postValue(it) }

    private fun flowExtendedLyricItem(): Flow<ExtendedLyricItem> = lyricIdFlow.combine(lyricLanguagesFlow) { id, languages ->
        val lyric = lyricsRepository.getLyricWithId(id)
        val extendedLyricItem = ExtendedLyricItem(lyric.lyricId, lyric.time,
            getSentenceFromLang(languages.mainLanguageId, lyric)!!,
            getSentenceFromLang(languages.translationLanguageId, lyric)!!,
            MovieItem("Movie Title", "Translated Title"))
        extendedLyricItem
    }

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

    fun searchLyricItem(lyricId: Int) = lyricIdFlow.tryEmit(lyricId)

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun getCurrentExtendedLyricItem() = currentExtendedLyricItem
}