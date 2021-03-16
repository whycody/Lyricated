package com.whycody.wordslife.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.data.lyrics.LyricsRepository

class SearchViewModel(private val lyricsRepository: LyricsRepository,
                      private val languageDao: LanguageDao): ViewModel() {

    private val lyricsItems = MutableLiveData<List<LyricItem>>()

    fun getLyricsItems() = lyricsItems

    fun searchWord(word: String) {
        lyricsItems.postValue(lyricsRepository.getLyricItemsWithWordIncluded(
                languageDao.getCurrentMainLanguage().id,
                languageDao.getCurrentTranslationLanguage().id,
                word))
    }
}