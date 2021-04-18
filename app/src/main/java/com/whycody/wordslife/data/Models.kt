package com.whycody.wordslife.data

import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.whycody.wordslife.search.SearchFragment
import com.whycody.wordslife.search.lyric.movie.MovieFragment
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

data class LyricItem(
        val lyricId: Int,
        val mainLangSentence: SpannableStringBuilder,
        val translatedSentence: String)

data class ExtendedLyricItem(
        val lyricId: Int,
        val time: String,
        val mainLangSentence: String,
        val translatedSentence: String,
        val movieItem: MovieItem)

data class MovieItem(
        val originalTitle: String,
        val translatedTitle: String? = null,
        val type: String = MovieFragment.MOVIE,
        val season: Int? = 0,
        val episode: Int? = 0)

data class LyricLanguages(
        val mainLanguageId: String,
        val translationLanguageId: String)

data class UserAction(
        val actionType: Int = SearchFragment.NO_ACTION,
        val actionId: Int = SearchFragment.NO_ACTION)

@Entity(tableName = "lyrics")
data class Lyric(
        @ColumnInfo(name = "lyric_id") @PrimaryKey val lyricId: Int,
        @ColumnInfo(name = "movie_id") val movieId: String,
        val time: String,
        val eng: String?,
        val pl: String?,
        val esp: String?,
        val fr: String?,
        val ger: String?,
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