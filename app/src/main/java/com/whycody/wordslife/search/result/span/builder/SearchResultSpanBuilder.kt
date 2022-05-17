package com.whycody.wordslife.search.result.span.builder

import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.Translation

interface SearchResultSpanBuilder {

    fun setLyricItemSpans(lyricItem: LyricItem)
}