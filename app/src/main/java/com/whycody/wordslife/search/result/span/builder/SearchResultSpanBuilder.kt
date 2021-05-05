package com.whycody.wordslife.search.result.span.builder

import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.Translation

interface SearchResultSpanBuilder {

    fun getSortedLyricItemsWithTranslationSpan(lyricItems: List<LyricItem>,
                                               translations: List<Translation>): List<LyricItem>

    fun setMainSentenceSpan(regex: Regex, lyric: LyricItem)
}