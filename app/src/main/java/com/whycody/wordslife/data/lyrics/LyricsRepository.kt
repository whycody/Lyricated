package com.whycody.wordslife.data.lyrics

import com.whycody.wordslife.data.LyricLanguages

class LyricsRepository(private val lyricsDao: LyricsDao,
                       private val queryBuilder: LyricsQueryBuilder) {

    fun getLyricWithId(lyricId: Int) = lyricsDao.getLyricWithId(lyricId)

    fun getLyricItemsWithWord(word: String, languages: LyricLanguages, queryLimit: Boolean = true) =
        lyricsDao.getLyricItemsWithWord(queryBuilder.getQuery(word, languages, queryLimit))
}