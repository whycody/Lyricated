package com.whycody.wordslife.search.lyric

import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.Lyric
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.data.lyrics.LyricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class LyricViewModel(private val lyricsRepository: LyricsRepository, languageDao: LanguageDao): ViewModel() {

    private val currentLyricItem = MutableLiveData<LyricItem>()
    private val lyricIdFlow = MutableStateFlow(0)
    private val lyricLanguagesFlow = MutableStateFlow(LyricLanguages(
            languageDao.getCurrentMainLanguage().id, languageDao.getCurrentTranslationLanguage().id))

    suspend fun collectLyricItem() = flowLyricItem().collect { currentLyricItem.postValue(it) }

    private fun flowLyricItem(): Flow<LyricItem> = lyricIdFlow.combine(lyricLanguagesFlow) { id, languages ->
        val lyric = lyricsRepository.getLyricWithId(id)
        val lyricItem = LyricItem(lyric.lyricId,
                SpannableStringBuilder(getSentenceFromLang(languages.mainLanguageId, lyric)),
                getSentenceFromLang(languages.translationLanguageId, lyric)!!)
        lyricItem
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

    fun getCurrentLyricItem() = currentLyricItem
}