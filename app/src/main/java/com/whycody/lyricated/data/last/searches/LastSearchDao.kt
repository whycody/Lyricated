package com.whycody.lyricated.data.last.searches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.whycody.lyricated.data.LastSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface LastSearchDao {

    @Query("SELECT * FROM last_searches WHERE id = :id")
    fun getLastSearchById(id: Int): LastSearch

    @Query("SELECT * FROM last_searches ORDER BY time DESC")
    fun getAllLastSearches(): List<LastSearch>

    @Query("SELECT * FROM last_searches ORDER BY time DESC LIMIT :limit OFFSET :offset")
    fun getPagedLastSearches(limit: Int, offset: Int): List<LastSearch>

    @Query("SELECT * FROM last_searches WHERE saved=1 ORDER BY time DESC LIMIT :limit OFFSET :offset")
    fun getPagedSavedLastSearches(limit: Int, offset: Int): List<LastSearch>

    @Query("SELECT * FROM last_searches ORDER BY time DESC LIMIT 4")
    fun flowFourLastSearches(): Flow<List<LastSearch>>

    @Query("SELECT * FROM last_searches ORDER BY time DESC")
    fun flowAllLastSearches(): Flow<List<LastSearch>>

    @Query("SELECT * FROM last_searches WHERE saved=1 ORDER BY time DESC")
    fun flowAllSavedLastSearches(): Flow<List<LastSearch>>

    @Query("UPDATE last_searches SET saved = :saved WHERE id = :id")
    fun updateLastSearchSaved(saved: Boolean, id: Int)

    @Query("UPDATE last_searches SET time = :time WHERE id = :id")
    fun refreshTime(time: Long, id: Int)

    @Insert
    fun insertLastSearch(lastSearch: LastSearch)

    @Query("DELETE FROM last_searches")
    fun deleteAllLastSearches()

    @Query("UPDATE last_searches SET saved = 0")
    fun deselectAllLastSearches()
}