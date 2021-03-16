package com.whycody.wordslife.data

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

@Entity(tableName = "last_searches")
data class LastSearch(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "main_language_id") val mainLanguageId: String,
        @ColumnInfo(name = "translation_language_id") val translationLanguageId: String,
        val text: String,
        val saved: Boolean = false,
        val time: Long = Calendar.getInstance().timeInMillis)