package com.whycody.wordslife.search.mapper

import com.whycody.wordslife.data.*
import com.whycody.wordslife.data.language.LanguageDao
import com.whycody.wordslife.search.result.span.builder.SearchResultSpanBuilder

class LyricItemMapperImpl(private val searchResultSpanBuilder: SearchResultSpanBuilder,
    private val languageDao: LanguageDao): LyricItemMapper {

    override fun getExtendedLyricItemFromLyricItem(lyricItem: LyricItem) =
        ExtendedLyricItem(lyricItem.lyricId, getSentenceWithoutSpecialChars(lyricItem.mainSentence),
            getSentenceWithoutSpecialChars(lyricItem.translatedSentence), lyricItem.movieId,
            getTimeInCorrectFormat(lyricItem.time), lyricItem.season, lyricItem.episode,
            LyricLanguages(lyricItem.mainLangId, lyricItem.translationLangId)
        )

    override fun getLyricItemFromLyric(lyric: Lyric,
                                       findLyricsResponse: FindLyricsResponse): LyricItem {
        val lyricItem = LyricItem(lyric.lyricId, lyric.mainSentence,
            lyric.translatedSentence, lyric.movie.id, lyric.time, getLyricSeason(lyric),
            getLyricEpisode(lyric), findLyricsResponse.mainLanguageId,
            findLyricsResponse.translationLanguageId)
        searchResultSpanBuilder.setLyricItemSpans(lyricItem)
        return lyricItem
    }

    override fun getExtendedLyricItemFromLyric(lyric: Lyric) =
        ExtendedLyricItem(lyric.lyricId, getSentenceWithoutSpecialChars(lyric.mainSentence),
            getSentenceWithoutSpecialChars(lyric.translatedSentence), lyric.movie.id,
            getTimeInCorrectFormat(lyric.time),
            getLyricSeason(lyric), getLyricEpisode(lyric),
            LyricLanguages(languageDao.getCurrentMainLanguage().id,
                languageDao.getCurrentTranslationLanguage().id))

    private fun getSentenceWithoutSpecialChars(oldSentence: String) =
        oldSentence.replace("¦", "")

    private fun getTimeInCorrectFormat(time: String): String {
        val totalSecs = time.toInt()
        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60
        val seconds = totalSecs % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private fun getLyricSeason(lyric: Lyric) =
        if(lyric.episode != null) lyric.episode.season else 0

    private fun getLyricEpisode(lyric: Lyric) =
        if(lyric.episode != null) lyric.episode.episode else 0
}