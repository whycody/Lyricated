package com.whycody.wordslife.data.lyrics

import com.whycody.wordslife.data.Lyric
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDaoImpl

class LyricsRepository (private val lyricsDao: LyricsDao) {

    fun getLyricItemsWithWordIncluded(mainLangId: String, transLangId: String, word: String, numberOfLyrics: Int = 250) =
            getLyricsWithWordIncluded(mainLangId, transLangId, getFormattedWord(word)).map { lyric -> LyricItem(
                    lyric.lyricId,
                    getSentenceFromLang(mainLangId, lyric)!!.replace("\n", ""),
                    getSentenceFromLang(transLangId, lyric)!!.replace("\n", "")) }
                    .distinctBy { it.mainLangSentence }
                    .take(numberOfLyrics)

    private fun getFormattedWord(word: String) = word.toLowerCase()
        .replace("*","[*]").replace("?", "[?]")

    private fun getLyricsWithWordIncluded(mainLangId: String, transLangId: String, word: String) =
        getLyricsWithWordIncludedInLanguage(mainLangId, word).filter {
            getSentenceFromLang(transLangId, it) != null
        }

    private fun getLyricsWithWordIncludedInLanguage(langId: String, word: String) =
        when(langId) {
            LanguageDaoImpl.PL -> lyricsDao.getPlLyricsWithWordIncluded(word)
            LanguageDaoImpl.ENG -> lyricsDao.getEngLyricsWithWordIncluded(word)
            LanguageDaoImpl.PT -> lyricsDao.getPtLyricsWithWordIncluded(word)
            LanguageDaoImpl.GER -> lyricsDao.getGerLyricsWithWordIncluded(word)
            LanguageDaoImpl.FR -> lyricsDao.getFrLyricsWithWordIncluded(word)
            LanguageDaoImpl.ESP -> lyricsDao.getEspLyricsWithWordIncluded(word)
            else -> lyricsDao.getItLyricsWithWordIncluded(word)
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
}