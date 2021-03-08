package com.whycody.wordslife.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.whycody.wordslife.data.last.searches.LastSearchDao

@Database(entities = [LastSearch::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun lastSearchDao(): LastSearchDao
}