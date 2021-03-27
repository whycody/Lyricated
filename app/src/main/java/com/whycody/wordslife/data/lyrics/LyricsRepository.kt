package com.whycody.wordslife.data.lyrics

import com.whycody.wordslife.data.language.LanguageDaoImpl

class LyricsRepository(private val lyricsDao: LyricsDao) {

    fun getLyricsWithWordIncludedInLanguage(langId: String, word: String) =
            when(langId) {
                LanguageDaoImpl.PL -> lyricsDao.getPlLyricsWithWordIncluded(word)
                LanguageDaoImpl.ENG -> lyricsDao.getEngLyricsWithWordIncluded(word)
                LanguageDaoImpl.PT -> lyricsDao.getPtLyricsWithWordIncluded(word)
                LanguageDaoImpl.GER -> lyricsDao.getGerLyricsWithWordIncluded(word)
                LanguageDaoImpl.FR -> lyricsDao.getFrLyricsWithWordIncluded(word)
                LanguageDaoImpl.ESP -> lyricsDao.getEspLyricsWithWordIncluded(word)
                else -> lyricsDao.getItLyricsWithWordIncluded(word)
            }
}