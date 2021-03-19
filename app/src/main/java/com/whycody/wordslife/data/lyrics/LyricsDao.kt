package com.whycody.wordslife.data.lyrics

import androidx.room.Dao
import androidx.room.Query
import com.whycody.wordslife.data.Lyric

@Dao
interface LyricsDao {

    @Query("SELECT * FROM lyrics WHERE LOWER(pl) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(pl) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getPlLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(eng) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(eng) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getEngLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(esp) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(esp) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getEspLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(it) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(it) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getItLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(ger) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(ger) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getGerLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(fr) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(fr) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getFrLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(pt) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(pt) GLOB :word || '[!-?[-`{-~| ]*'")
    fun getPtLyricsWithWordIncluded(word: String): List<Lyric>
}