package com.whycody.lyricated.data

import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.whycody.lyricated.data.filter.FilterDaoImpl
import com.whycody.lyricated.data.language.LanguageDaoImpl
import com.whycody.lyricated.data.settings.SettingsDaoImpl
import com.whycody.lyricated.data.sort.SortDaoImpl
import com.whycody.lyricated.data.studymode.StudyModeDaoImpl
import com.whycody.lyricated.search.SearchFragment
import com.whycody.lyricated.search.translation.TranslationViewModel
import java.io.Serializable
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
        var educationalMaterials: String = SettingsDaoImpl.SHOW_EDU,
        var lyricLanguages: LyricLanguages = LyricLanguages(),
        var sortOptionId: String = SortDaoImpl.BEST_MATCH,
        var checkedFilters: List<String> = FilterDaoImpl.DEFAULT_FILTERS,
        var chosenSource: String? = null)

data class AppConfiguration(
        var appearance: String = SettingsDaoImpl.DEFAULT,
        var educationalMaterials: String = SettingsDaoImpl.SHOW_EDU,
        var history: String = SettingsDaoImpl.SAVE_HISTORY,
        var studyModeDifficulty: String = StudyModeDaoImpl.MEDIUM)

data class ConfigurationItem(
        var name: String,
        var confId: String,
        val type: String)

data class LibraryItem(
        val id: String,
        val title: String,
        val desc: String)

data class Translation(
        val translatedPhrase: String? = null,
        var numberOfUsages: Int = 0,
        var translationLangId: String? = null,
        var type: Int = TranslationViewModel.TRANSLATION)

data class LyricItem(
        val lyricId: Int,
        val mainSentence: String,
        val translatedSentence: String,
        val movieId: String,
        val time: String,
        val episode: Episode?,
        val mainLangId: String,
        val translationLangId: String,
        @Ignore var mainSentenceSpan: SpannableStringBuilder?,
        @Ignore var translatedSentenceSpan: SpannableStringBuilder?) {

    constructor(lyricId: Int, mainSentence: String, translatedSentence: String, movieId: String,
                time:String, episode: Episode?, mainLangId: String, translationLangId: String) :
            this(lyricId, mainSentence, translatedSentence, movieId, time, episode,
                    mainLangId, translationLangId, mainSentenceSpan = null,
                    translatedSentenceSpan = null)
}

data class ExtendedLyricItem(
        val lyricId: Int,
        val mainLangSentence: String?,
        val translatedSentence: String?,
        val movieId: String,
        val time: String,
        val episode: Episode?,
        val languages: LyricLanguages): Serializable

data class MovieItem(
        val mainTitle: String,
        val translatedTitle: String? = null,
        val netflixid: Int? = null,
        val type: String,
        val episodeItem: Episode?,
        val time: String?)

data class MovieListItem(
        val id: String,
        val type: String,
        val netflixid: Int?,
        val title: String,
        val subtitle: String?,
        val allTitles: String,
        var isChecked: Boolean = false)

data class VocabularyItem(
        val index: Int,
        val word: String)

data class ExtendedVocabularyItem(
        val index: Int,
        val word: String,
        val shown: Boolean)

data class TranslationItem(
        val drawable: Drawable,
        val translatedSentence: String?)

data class LyricLanguages(
        var mainLangId: String = LanguageDaoImpl.UNSET,
        var translationLangId: String = LanguageDaoImpl.UNSET): Serializable

data class UserAction(
        val actionType: Int = SearchFragment.NO_ACTION,
        val actionId: Int = SearchFragment.NO_ACTION,
        var clickedExtendedLyricItem: ExtendedLyricItem? = null)

data class SortItem(
        val id: String,
        val headerName: String,
        val options: List<SortOption>,
        val expanded: Boolean = true)

data class SortOption(
        val id: String,
        val name: String,
        var isChecked: Boolean = false)

@Entity(tableName = "movies")
data class Movie(
        @ColumnInfo(name = "movie_id") @PrimaryKey val id: String,
        val lang: String,
        val type: String,
        val minutes: Int,
        val netflixid: Int?,
        val lyrics: Int,
        val eng: String?,
        val pl: String?,
        val esp: String?,
        val fr: String?,
        val ger: String?,
        val it: String?,
        val pt: String?)

data class MoviesResponse(
        val movies: List<MovieApi>)

data class MovieApi(
        val id: String,
        val lang: String,
        val type: String,
        val minutes: Int,
        val netflixid: Int?,
        val en: String?,
        val pl: String?,
        val esp: String?,
        val fr: String?,
        val ger: String?,
        val it: String?,
        val pt: String?)

data class FindLyricsResponse(
        @SerializedName("main_language_id")
        val mainLanguageId: String,
        @SerializedName("translation_language_id")
        val translationLanguageId: String,
        @SerializedName("search_word")
        val searchWord: String,
        @SerializedName("translations")
        val translations: List<String>,
        @SerializedName("main_results")
        val mainResults: List<Lyric>,
        @SerializedName("similar_results")
        val similarResults: List<Lyric>)

data class GetRandomLyricBody(
        @SerializedName("main_language_id")
        var mainLanguageId: String,
        @SerializedName("translation_language_id")
        var translationLanguageId: String)

data class FindLyricsBody(
        @SerializedName("searched_phrase")
        var searchedPhrase: String,
        @SerializedName("main_language_id")
        var mainLanguageId: String,
        @SerializedName("translation_language_id")
        var translationLanguageId: String,
        @SerializedName("sorting_mode")
        var sortingMode: String = SortDaoImpl.BEST_MATCH,
        @SerializedName("curses")
        var curses: Boolean = true,
        @SerializedName("source")
        var source: String? = null,
        @SerializedName("movie_id")
        var movieId: String? = null)

data class Lyric(
        @SerializedName("id")
        val lyricId: Int,
        @SerializedName("main_sentence")
        val mainSentence: String,
        @SerializedName("translated_sentence")
        val translatedSentence: String,
        val time: String,
        val movie: MovieApi,
        val episode: Episode?)

data class Episode(
        val season: Int,
        val episode: Int,
        val netflixid: Int?): Serializable

@Entity(tableName = "last_searches")
data class LastSearch(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "main_language_id") val mainLanguageId: String,
        @ColumnInfo(name = "translation_language_id") val translationLanguageId: String,
        val text: String,
        val saved: Boolean = false,
        val time: Long = Calendar.getInstance().timeInMillis)

data class SetLyricQualityBody(
        @SerializedName("lyric_id")
        val lyricId: Int,
        val quality: Int)

data class SetLyricQualityResponse(
        val id: Int,
        val quality: Int,
        val success: Boolean)

data class DatabaseVersionResponse(
        val version: Int
)