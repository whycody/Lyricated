package com.whycody.wordslife.data.lyrics

import androidx.room.Dao
import androidx.room.Query
import com.whycody.wordslife.data.Lyric

@Dao
interface LyricsDao {

    @Query("SELECT * FROM lyrics WHERE LOWER(pl) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(pl) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getPlLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(eng) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(eng) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getEngLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(esp) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(esp) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getEspLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(it) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(it) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getItLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(ger) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(ger) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getGerLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(fr) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(fr) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getFrLyricsWithWordIncluded(word: String): List<Lyric>

    @Query("SELECT * FROM lyrics WHERE LOWER(pt) GLOB '*[!-?[-`{-~| ]' || :word || '[!-?[-`{-~| ]*' OR LOWER(pt) GLOB :word || '[!-?[-`{-~| ]*' LIMIT 250")
    fun getPtLyricsWithWordIncluded(word: String): List<Lyric>
}