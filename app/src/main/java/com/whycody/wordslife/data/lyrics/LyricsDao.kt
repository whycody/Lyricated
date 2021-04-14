package com.whycody.wordslife.data.lyrics

import androidx.room.Dao
import androidx.room.Query
import com.whycody.wordslife.data.Lyric

@Dao
interface LyricsDao {

    @Query("SELECT * FROM lyrics WHERE pl LIKE '%' || :word || '%' LIMIT 1000")
    fun getPlLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE eng LIKE '%' || :word || '%' LIMIT 1000")
    fun getEngLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE esp LIKE '%' || :word || '%' LIMIT 1000")
    fun getEspLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE it LIKE '%' || :word || '%' LIMIT 1000")
    fun getItLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE ger LIKE '%' || :word || '%' LIMIT 1000")
    fun getGerLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE fr LIKE '%' || :word || '%' LIMIT 1000")
    fun getFrLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE pt LIKE '%' || :word || '%' LIMIT 1000")
    fun getPtLyricsWithWordIncluded(word: String): List<Lyric>
}