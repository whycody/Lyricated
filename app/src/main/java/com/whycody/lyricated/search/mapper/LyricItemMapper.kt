package com.whycody.lyricated.search.mapper

import com.whycody.lyricated.data.ExtendedLyricItem
import com.whycody.lyricated.data.FindLyricsResponse
import com.whycody.lyricated.data.Lyric
import com.whycody.lyricated.data.LyricItem

interface LyricItemMapper {

    fun getExtendedLyricItemFromLyricItem(lyricItem: LyricItem): ExtendedLyricItem

    fun getLyricItemFromLyric(lyric: Lyric, findLyricsResponse: FindLyricsResponse): LyricItem

    fun getExtendedLyricItemFromLyric(lyric: Lyric): ExtendedLyricItem
}