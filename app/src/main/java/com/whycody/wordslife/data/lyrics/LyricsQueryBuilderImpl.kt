package com.whycody.wordslife.data.lyrics

import androidx.sqlite.db.SimpleSQLiteQuery
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SearchConfiguration
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.sort.SortDaoImpl
import java.lang.StringBuilder

class LyricsQueryBuilderImpl(private val searchConfigurationDao: SearchConfigurationDao): LyricsQueryBuilder {

    override fun getQuery(word: String, languages: LyricLanguages, queryLimit: Boolean) =
        SimpleSQLiteQuery(buildQuery(word, languages, queryLimit))

    private fun buildQuery(word: String, languages: LyricLanguages, queryLimit: Boolean): String {
        val searchConfig = searchConfigurationDao.getSearchConfiguration()
        with(StringBuilder()) {
            append(getDefaultStartOfQuery(languages))
            append(getDefaultConditions(word, languages))
            append(getDefaultGrouping(languages.mainLangId))
            append(getOrderingQuery(searchConfig, languages))
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

    private fun getOrderingQuery(searchConfiguration: SearchConfiguration, languages: LyricLanguages) =
        when(searchConfiguration.sortOptionId) {
            SortDaoImpl.BEST_MATCH -> getDefaultOrdering(languages)
            SortDaoImpl.SHORTEST -> getShortestOrdering(languages)
            else -> getLongestOrdering(languages)
        }

    private fun getDefaultOrdering(languages: LyricLanguages) =
            " ORDER BY ABS(LENGTH(${languages.mainLangId})-LENGTH(${languages.translationLangId}))," +
                    " ABS(ROUND(LENGTH(${languages.mainLangId})/10)*10-25)"

    private fun getShortestOrdering(languages: LyricLanguages) =
        " ORDER BY LENGTH(${languages.mainLangId}) ASC"

    private fun getLongestOrdering(languages: LyricLanguages) =
        " ORDER BY LENGTH(${languages.mainLangId}) DESC"

    private fun getDefaultLimit() = " LIMIT $DEFAULT_QUERY_LIMIT"

    companion object {
        const val DEFAULT_QUERY_LIMIT = 30000
    }
}