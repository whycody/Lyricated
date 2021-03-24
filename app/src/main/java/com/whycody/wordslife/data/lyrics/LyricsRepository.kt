package com.whycody.wordslife.data.lyrics

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import com.whycody.wordslife.data.Lyric
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.search.result.SearchResultFragment

class LyricsRepository(private val lyricsDao: LyricsDao) {

    fun getSimilarLyricItemsWithWordIncluded(mainLangId: String, transLangId: String, word: String, numberOfLyrics: Int = 250) =
            getSimilarLyricsWithWordIncluded(mainLangId, transLangId, getFormattedWord(word)).map {
               getLyricItemFromLyric(mainLangId, transLangId, word, SearchResultFragment.SIMILAR_LYRICS, it)
           }.distinctBy { it.mainLangSentence }.take(numberOfLyrics)

    private fun getSimilarLyricsWithWordIncluded(mainLangId: String, transLangId: String, word: String): List<Lyric> {
        val regex = Regex(getSimilarLyricsPattern(word))
        return getLyricsWithWordIncludedInLanguage(mainLangId, word.substring(0, word.length-1)).filter {
            getSentenceFromLang(transLangId, it) != null &&
                    regex.find(getSentenceFromLang(mainLangId, it)!!.toLowerCase()) != null
        }
    }

    private fun getSimilarLyricsPattern(word: String) =
            when {
                word.length > 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,? ]\\S*|\\b${word.substring(0, word.length-1)}\\b"
                word.length == 4 -> "\\b\\S$word\\S*|\\b\\S?$word?[^${word[word.length - 1]}.,? ]\\S*"
                else -> "\\b\\S$word\\S?[^\\s]*|\\b\\S?$word[^.,? ][^\\s]*"
            }

    fun getMainLyricItemsWithWordIncluded(mainLangId: String, transLangId: String, word: String, numberOfLyrics: Int = 250) =
            getMainLyricsWithWordIncluded(mainLangId, transLangId, getFormattedWord(word)).map {
                getLyricItemFromLyric(mainLangId, transLangId, word, SearchResultFragment.MAIN_LYRICS, it)
            }.distinctBy { it.mainLangSentence }.take(numberOfLyrics)

    private fun getMainLyricsWithWordIncluded(mainLangId: String, transLangId: String, word: String): List<Lyric> {
        val regex = Regex(getMainLyricsPattern(word))
        return getLyricsWithWordIncludedInLanguage(mainLangId, word).filter {
            getSentenceFromLang(transLangId, it) != null &&
                    regex.find(getSentenceFromLang(mainLangId, it)!!.toLowerCase()) != null
        }
    }

    private fun getMainLyricsPattern(word: String) = "\\b$word\\b"

    private fun getLyricItemFromLyric(mainLangId: String, transLangId: String,
                                      word: String, typeOfLyrics: String, lyric: Lyric): LyricItem {
        val mainSentence = getSentenceFromLang(mainLangId, lyric)!!
        val translatedSentence = getSentenceFromLang(transLangId, lyric)!!
        return LyricItem(lyric.lyricId, getMainSentenceSpan(mainSentence, word, typeOfLyrics), translatedSentence)
    }

    private fun getMainSentenceSpan(mainSentence: String, word: String, typeOfLyrics: String): SpannableStringBuilder {
        val stb = SpannableStringBuilder(mainSentence)
        val regex = Regex(getPattern(typeOfLyrics, getFormattedWord(word)))
        regex.findAll(mainSentence.toLowerCase()).forEach{
            stb.setSpan(StyleSpan(Typeface.BOLD), it.range.first,
                    it.range.last+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return stb
    }

    private fun getPattern(typeOfLyrics: String, word: String) =
            if(typeOfLyrics == SearchResultFragment.MAIN_LYRICS) getMainLyricsPattern(word)
            else getSimilarLyricsPattern(word)

    private fun getFormattedWord(word: String) = word.toLowerCase().trim()
            .replace("*", "").replace("?", "")
            .replace(".", "")

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