package com.whycody.wordslife.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whycody.wordslife.data.last.searches.LastSearchDao
import com.whycody.wordslife.data.lyrics.LyricsDao
import com.whycody.wordslife.data.movie.EpisodeDao
import com.whycody.wordslife.data.movie.MovieDao

@Database(entities = [Lyric::class, Movie::class, Episode::class, LastSearch::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun lastSearchDao(): LastSearchDao

    abstract fun lyricsDao(): LyricsDao

    abstract fun movieDao(): MovieDao

    abstract fun episodeDao(): EpisodeDao
}