package com.whycody.wordslife.data.lyrics

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.whycody.wordslife.data.Lyric
import com.whycody.wordslife.data.LyricItem

@Dao
interface LyricsDao {

    @Query("SELECT * FROM lyrics WHERE :lyricId=lyric_id")
    fun getLyricWithId(lyricId: Int): Lyric

    @RawQuery
    fun getLyricItemsWithWord(query: SimpleSQLiteQuery): List<LyricItem>
}