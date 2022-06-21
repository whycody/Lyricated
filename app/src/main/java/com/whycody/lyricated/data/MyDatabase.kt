package com.whycody.lyricated.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whycody.lyricated.data.last.searches.LastSearchDao
import com.whycody.lyricated.data.movie.MovieDao

@Database(entities = [Movie::class, LastSearch::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun lastSearchDao(): LastSearchDao

    abstract fun movieDao(): MovieDao
}