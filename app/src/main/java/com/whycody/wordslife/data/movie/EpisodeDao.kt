package com.whycody.wordslife.data.movie

import androidx.room.Dao
import androidx.room.Query
import com.whycody.wordslife.data.Episode

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episodes WHERE :lyricId BETWEEN first_lyric_id AND last_lyric_id")
    fun getEpisodeWithLyricId(lyricId: Int): Episode?
}