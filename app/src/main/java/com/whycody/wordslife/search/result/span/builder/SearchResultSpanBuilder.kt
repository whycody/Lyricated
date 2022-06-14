package com.whycody.wordslife.search.result.span.builder

import com.whycody.wordslife.data.LyricItem

interface SearchResultSpanBuilder {

    fun setLyricItemSpans(lyricItem: LyricItem)
}