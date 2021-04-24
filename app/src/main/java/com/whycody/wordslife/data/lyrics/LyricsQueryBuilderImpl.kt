package com.whycody.wordslife.data.lyrics

import androidx.sqlite.db.SimpleSQLiteQuery
import com.whycody.wordslife.data.LyricLanguages
import java.lang.StringBuilder

class LyricsQueryBuilderImpl: LyricsQueryBuilder {

    override fun getQuery(word: String, languages: LyricLanguages, queryLimit: Boolean) =
        SimpleSQLiteQuery(buildQuery(word, languages, queryLimit))

    private fun buildQuery(word: String, languages: LyricLanguages, queryLimit: Boolean): String {
        with(StringBuilder()) {
            append(getDefaultStartOfQuery(languages))
            append(getDefaultConditions(word, languages))
            append(getDefaultGrouping(languages.mainLangId))
            append(getDefaultOrdering(languages))
            if(queryLimit) append(getDefaultLimit())
            append(";")
            return this.toString()
        }
    }

    private fun getDefaultStartOfQuery(languages: LyricLanguages) =
            "SELECT lyric_id AS lyricId, ${languages.mainLangId} AS mainSentence," +
                    " ${languages.translationLangId} AS translatedSentence FROM lyrics"

    private fun getDefaultConditions(word: String, languages: LyricLanguages) =
            " WHERE ${languages.mainLangId} LIKE \"%$word%\" AND ${languages.translationLangId} IS NOT NULL"

    private fun getDefaultGrouping(mainLangId: String) =
            " GROUP BY LOWER($mainLangId)"

    private fun getDefaultOrdering(languages: LyricLanguages) =
            " ORDER BY ABS(LENGTH(${languages.mainLangId})-LENGTH(${languages.translationLangId}))," +
                    " ABS(ROUND(LENGTH(${languages.mainLangId})/10)*10-25)"

    private fun getDefaultLimit() = " LIMIT $DEFAULT_QUERY_LIMIT"

    companion object {
        const val DEFAULT_QUERY_LIMIT = 30000
    }
}