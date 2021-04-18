package com.whycody.wordslife.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whycody.wordslife.data.last.searches.LastSearchDao
import com.whycody.wordslife.data.lyrics.LyricsDao

@Database(entities = [Lyric::class, Movie::class, Episode::class, LastSearch::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun lastSearchDao(): LastSearchDao

    abstract fun lyricsDao(): LyricsDao
}