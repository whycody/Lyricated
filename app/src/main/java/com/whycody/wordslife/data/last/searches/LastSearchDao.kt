package com.whycody.wordslife.data.last.searches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.whycody.wordslife.data.LastSearch

@Dao
interface LastSearchDao {

    @Query("SELECT * FROM last_searches ORDER BY id DESC")
    fun getAllLastSearches(): List<LastSearch>

    @Query("SELECT * FROM last_searches ORDER BY id DESC LIMIT 4")
    fun getFourLastSearches(): List<LastSearch>

    @Insert
    fun insertLastSearch(lastSearch: LastSearch)
}