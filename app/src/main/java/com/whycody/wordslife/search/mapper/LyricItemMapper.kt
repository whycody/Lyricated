package com.whycody.wordslife.search.mapper

import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.FindLyricsResponse
import com.whycody.wordslife.data.Lyric
import com.whycody.wordslife.data.LyricItem

interface LyricItemMapper {

    fun getExtendedLyricItemFromLyricItem(lyricItem: LyricItem): ExtendedLyricItem

    fun getLyricItemFromLyric(lyric: Lyric, findLyricsResponse: FindLyricsResponse): LyricItem

    fun getExtendedLyricItemFromLyric(lyric: Lyric): ExtendedLyricItem
}