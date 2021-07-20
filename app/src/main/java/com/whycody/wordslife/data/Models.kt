package com.whycody.wordslife.data

import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.beust.klaxon.Json
import com.whycody.wordslife.data.language.LanguageDaoImpl
import com.whycody.wordslife.search.SearchFragment
import com.whycody.wordslife.search.translation.TranslationViewModel
import java.util.*

data class Language(
        var id: String,
        var name: String,
        var drawable: Drawable)

data class HistoryItem(
        var id: Int,
        var text: String,
        var mainLanguage: Drawable,
        var translationLanguage: Drawable,
        var saved: Boolean = false)

data class SearchConfiguration(
        var lyricLanguages: LyricLanguages = LyricLanguages())

data class Translation(
        @Json(name = "text") val translatedPhrase: String? = null,
        @Json(ignored = true) var numberOfUsages: Int = 0,
        @Json(ignored = true) var translationLangId: String? = null,
        @Json(ignored = true) var type: Int = TranslationViewModel.TRANSLATION)

data class TranslationQuery(
        val status: String,
        val message: String,
        val result: List<String>? = null)

data class LyricItem(
        val lyricId: Int,
        val mainSentence: String,
        val translatedSentence: String,
        @Ignore var mainSentenceSpan: SpannableStringBuilder?,
        @Ignore var translatedSentenceSpan: SpannableStringBuilder?,
        @Ignore var translationAvailable: Boolean = false) {

    constructor(lyricId: Int, mainSentence: String, translatedSentence: String) :
            this(lyricId, mainSentence, translatedSentence, null, SpannableStringBuilder(translatedSentence))
}

data class ExtendedLyricItem(
        val lyricId: Int,
        val mainLangSentence: String?,
        val translatedSentence: String?,
        val movieId: String,
        val time: String,
        val languages: LyricLanguages)

data class MovieItem(
        val mainTitle: String,
        val translatedTitle: String? = null,
        val type: String,
        val episodeItem: EpisodeItem?,
        val time: String?)

data class EpisodeItem(
        val season: Int,
        val episode: Int)

data class VocabularyItem(
        val index: Int,
        val word: String)

data class TranslationItem(
        val drawable: Drawable,
        val translatedSentence: String?)

data class LyricLanguages(
        var mainLangId: String = LanguageDaoImpl.DEFAULT_MAIN_LANGUAGE,
        var translationLangId: String = LanguageDaoImpl.DEFAULT_TRANSLATION_LANGUAGE)

data class UserAction(
        val actionType: Int = SearchFragment.NO_ACTION,
        val actionId: Int = SearchFragment.NO_ACTION)

data class SortItem(
        val headerName: String,
        val options: List<SortOption>)

data class SortOption(
        val id: String,
        val name: String)

@Entity(tableName = "lyrics")
data class Lyric(
        @ColumnInfo(name = "lyric_id") @PrimaryKey val lyricId: Int,
        @ColumnInfo(name = "movie_id") val movieId: String,
        val time: String,
        val en: String?,
        val pl: String?,
        val es: String?,
        val fr: String?,
        val de: String?,
        val it: String?,
        val pt: String?)

@Entity(tableName = "movies")
data class Movie(
        @ColumnInfo(name = "movie_id") @PrimaryKey val movieId: String,
        val lang: String,
        val type: String,
        val minutes: Int,
        val lyrics: Int,
        val eng: String?,
        val pl: String?,
        val esp: String?,
        val fr: String?,
        val ger: String?,
        val it: String?,
        val pt: String?)

@Entity(tableName = "episodes")
data class Episode(
        @ColumnInfo(name = "episode_id") @PrimaryKey val episodeId: Int,
        @ColumnInfo(name = "series_id") val seriesId: String,
        @ColumnInfo(name = "first_lyric_id") val firstLyricId: Int,
        @ColumnInfo(name = "last_lyric_id") val lastLyricId: Int,
        val season: Int,
        val episode: Int)

@Entity(tableName = "last_searches")
data class LastSearch(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "main_language_id") val mainLanguageId: String,
        @ColumnInfo(name = "translation_language_id") val translationLanguageId: String,
        val text: String,
        val saved: Boolean = false,
        val time: Long = Calendar.getInstance().timeInMillis)