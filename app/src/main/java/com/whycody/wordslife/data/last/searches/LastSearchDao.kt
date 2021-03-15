package com.whycody.wordslife.data.last.searches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.whycody.wordslife.data.LastSearch

@Dao
interface LastSearchDao {

    @Query("SELECT * FROM last_searches WHERE id = :id")
    fun getLastSearchById(id: Int): LastSearch

    @Query("SELECT * FROM last_searches ORDER BY time DESC")
    fun getAllLastSearches(): List<LastSearch>

    @Query("SELECT * FROM last_searches ORDER BY time DESC LIMIT 4")
    fun getFourLastSearches(): List<LastSearch>

    @Query("UPDATE last_searches SET saved = :saved WHERE id = :id")
    fun updateLastSearchSaved(saved: Boolean, id: Int)

    @Query("UPDATE last_searches SET time = :time WHERE id = :id")
    fun refreshTime(time: Long, id: Int)

    @Insert
    fun insertLastSearch(lastSearch: LastSearch)
}