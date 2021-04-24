package com.whycody.wordslife.data.lyrics

import androidx.sqlite.db.SimpleSQLiteQuery
import com.whycody.wordslife.data.LyricLanguages

interface LyricsQueryBuilder {

    fun getQuery(word: String, languages: LyricLanguages, queryLimit: Boolean): SimpleSQLiteQuery
}