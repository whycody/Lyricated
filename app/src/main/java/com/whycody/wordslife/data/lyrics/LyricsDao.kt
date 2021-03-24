package com.whycody.wordslife.data.lyrics

import androidx.room.Dao
import androidx.room.Query
import com.whycody.wordslife.data.Lyric

@Dao
interface LyricsDao {

    @Query("SELECT * FROM lyrics WHERE pl LIKE '%' || :word || '%'")
    fun getPlLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE eng LIKE '%' || :word || '%'")
    fun getEngLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE esp LIKE '%' || :word || '%'")
    fun getEspLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE it LIKE '%' || :word || '%'")
    fun getItLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE ger LIKE '%' || :word || '%'")
    fun getGerLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE fr LIKE '%' || :word || '%'")
    fun getFrLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE pt LIKE '%' || :word || '%'")
    fun getPtLyricsWithWordIncluded(word: String): List<Lyric>
}