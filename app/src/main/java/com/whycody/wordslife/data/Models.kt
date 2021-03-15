package com.whycody.wordslife.data

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

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

@Entity(tableName = "last_searches")
data class LastSearch(
        @PrimaryKey(autoGenerate = true) @NotNull val id: Int = 0,
        @NotNull @ColumnInfo(name = "main_language_id") val mainLanguageId: String,
        @NotNull @ColumnInfo(name = "translation_language_id") val translationLanguageId: String,
        @NotNull val text: String,
        @NotNull val saved: Boolean = false)