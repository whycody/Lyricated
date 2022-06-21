package com.whycody.lyricated.search.result.span.builder

import com.whycody.lyricated.data.LyricItem

interface SearchResultSpanBuilder {

    fun setLyricItemSpans(lyricItem: LyricItem)
}