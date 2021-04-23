package com.whycody.wordslife.search.lyric

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class LyricViewModel(private val lyricsRepository: LyricsRepository): ViewModel() {

    private val currentExtendedLyricItem = MutableLiveData<ExtendedLyricItem>()
    private val lyricIdFlow = MutableStateFlow(0)
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages())

    suspend fun collectExtendedLyricItem() = flowExtendedLyricItem().collect {
        currentExtendedLyricItem.postValue(it)
    }

    private fun flowExtendedLyricItem(): Flow<ExtendedLyricItem> =
            lyricIdFlow.combine(lyricLanguagesFlow) { id, languages ->
        val lyric = lyricsRepository.getLyricWithId(id)
        getExtendedLyricItemFromLyric(lyric, languages)
    }

    private fun getExtendedLyricItemFromLyric(lyric: Lyric, languages: LyricLanguages) =
            ExtendedLyricItem(lyric.lyricId,
                    getSentenceFromLyricInLang(languages.mainLanguageId, lyric),
                    getSentenceFromLyricInLang(languages.translationLanguageId, lyric),
                    lyric.movieId,
                    lyric.time,
                    languages)

    private fun getSentenceFromLyricInLang(langId: String, lyric: Lyric) =
            when(langId) {
                LanguageDaoImpl.PL -> lyric.pl
                LanguageDaoImpl.ENG -> lyric.eng
                LanguageDaoImpl.PT -> lyric.pt
                LanguageDaoImpl.GER -> lyric.ger
                LanguageDaoImpl.FR -> lyric.fr
                LanguageDaoImpl.ESP -> lyric.esp
                LanguageDaoImpl.IT -> lyric.it
                else -> null
            }

    fun searchLyricItem(lyricId: Int) = lyricIdFlow.tryEmit(lyricId)

    fun setLyricLanguages(lyricLanguages: LyricLanguages) = lyricLanguagesFlow.tryEmit(lyricLanguages)

    fun getCurrentExtendedLyricItem() = currentExtendedLyricItem
}