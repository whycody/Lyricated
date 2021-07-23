package com.whycody.wordslife.data.lyrics

import androidx.sqlite.db.SimpleSQLiteQuery
import com.whycody.wordslife.data.LyricLanguages
import com.whycody.wordslife.data.SearchConfiguration
import com.whycody.wordslife.data.filter.FilterDaoImpl
import com.whycody.wordslife.data.search.configuration.SearchConfigurationDao
import com.whycody.wordslife.data.sort.SortDaoImpl
import java.lang.StringBuilder

class LyricsQueryBuilderImpl(private val searchConfigurationDao: SearchConfigurationDao): LyricsQueryBuilder {

    override fun getQuery(word: String, languages: LyricLanguages, queryLimit: Boolean) =
        SimpleSQLiteQuery(buildQuery(word, languages, queryLimit))

    private fun buildQuery(word: String, languages: LyricLanguages, queryLimit: Boolean): String {
        val searchConfig = searchConfigurationDao.getSearchConfiguration()
        val onlySeries = searchConfig.checkedFilters.contains(FilterDaoImpl.ONLY_SERIES)
        val onlyMovies = searchConfig.checkedFilters.contains(FilterDaoImpl.ONLY_MOVIES)
        val typeOfProductionFilter = onlyMovies || onlySeries
        with(StringBuilder()) {
            if(typeOfProductionFilter) append(getStartOfQueryWithMovies(languages))
            else append(getDefaultStartOfQuery(languages))
            if(typeOfProductionFilter) append(getInnerJoinWithMovies())
            append(getDefaultConditions(word, languages))
            if(onlySeries) append(getOnlySeriesCondition())
            if(onlyMovies) append(getOnlyMoviesCondition())
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

    private fun getStartOfQueryWithMovies(languages: LyricLanguages) =
        "SELECT lyrics.lyric_id AS lyricId, lyrics.${languages.mainLangId} AS mainSentence," +
                " lyrics.${languages.translationLangId} AS translatedSentence, movies.type FROM lyrics"

    private fun getDefaultConditions(word: String, languages: LyricLanguages) =
            " WHERE lyrics.${languages.mainLangId} LIKE \"%$word%\" AND lyrics.${languages.translationLangId} IS NOT NULL"

    private fun getOnlySeriesCondition() = " AND movies.type = \"serie\""

    private fun getOnlyMoviesCondition() = " AND movies.type = \"movie\""

    private fun getDefaultGrouping(mainLangId: String) =
            " GROUP BY LOWER(lyrics.$mainLangId)"

    private fun getOrderingQuery(searchConfiguration: SearchConfiguration, languages: LyricLanguages) =
        when(searchConfiguration.sortOptionId) {
            SortDaoImpl.BEST_MATCH -> getDefaultOrdering(languages)
            SortDaoImpl.SHORTEST -> getShortestOrdering(languages)
            else -> getLongestOrdering(languages)
        }

    private fun getDefaultOrdering(languages: LyricLanguages) =
            " ORDER BY ABS(LENGTH(lyrics.${languages.mainLangId})-LENGTH(lyrics.${languages.translationLangId}))," +
                    " ABS(ROUND(LENGTH(lyrics.${languages.mainLangId})/10)*10-25)"

    private fun getShortestOrdering(languages: LyricLanguages) =
        " ORDER BY LENGTH(lyrics.${languages.mainLangId}) ASC"

    private fun getLongestOrdering(languages: LyricLanguages) =
        " ORDER BY LENGTH(lyrics.${languages.mainLangId}) DESC"

    private fun getInnerJoinWithMovies() =
        " INNER JOIN movies ON lyrics.movie_id = movies.movie_id"

    private fun getDefaultLimit() = " LIMIT $DEFAULT_QUERY_LIMIT"

    companion object {
        const val DEFAULT_QUERY_LIMIT = 30000
    }
}