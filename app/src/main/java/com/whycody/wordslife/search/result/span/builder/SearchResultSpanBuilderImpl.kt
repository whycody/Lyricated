package com.whycody.wordslife.search.result.span.builder

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LyricItem
import com.whycody.wordslife.data.Translation
import org.koin.android.ext.koin.androidContext

class SearchResultSpanBuilderImpl(private val context: Context): SearchResultSpanBuilder {

    override fun setMainSentenceSpan(regex: Regex, lyric: LyricItem) {
        val stb = SpannableStringBuilder(lyric.mainSentence)
        regex.findAll(lyric.mainSentence).forEach { stb.setSpan(StyleSpan(Typeface.BOLD),
                it.range.first, it.range.last + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
        lyric.mainSentenceSpan = stb
    }

    override fun getSortedLyricItemsWithTranslationSpan(lyricItems: List<LyricItem>,
                                                        translations: List<Translation>): List<LyricItem> {
        lyricItems.forEach {
            it.translationAvailable = setTranslatedSpansInLyric(it,
                getFormatTranslations(translations)) >= 1
        }
        return lyricItems.sortedByDescending { it.translationAvailable }
    }

    private fun getFormatTranslations(translations: List<Translation>) =
        translations.map {
            Translation(
                it.translatedPhrase?.replace("(", "\\(")?.replace(")", "\\)"),
                it.numberOfUsages, it.translationLangId, it.type
            )
        }

    private fun setTranslatedSpansInLyric(lyricItem: LyricItem, translations: List<Translation>): Int {
        lyricItem.translatedSentenceSpan = SpannableStringBuilder(lyricItem.translatedSentence)
        val numberOfSpansInMainSent = getNumberOfSpansInSent(lyricItem.mainSentenceSpan!!)
        var numberOfSpansInTranslatedSent = 0
        translations.forEach { translation ->
            if(numberOfSpansInTranslatedSent >= numberOfSpansInMainSent!!) return@forEach
            numberOfSpansInTranslatedSent += setStbWithTranslation(lyricItem, translation)
        }
        return numberOfSpansInTranslatedSent
    }

    private fun setStbWithTranslation(lyricItem: LyricItem, translation: Translation): Int {
        var numberOfSpansInTranslatedSent = 0
        val regex = Regex(getTranslatedSentencePattern(translation.translatedPhrase!!), RegexOption.IGNORE_CASE)
        val foundResults = regex.findAll(lyricItem.translatedSentence)
        foundResults.forEach {
            numberOfSpansInTranslatedSent += fillFoundResult(it, translation, lyricItem.translatedSentenceSpan!!)
        }
        return numberOfSpansInTranslatedSent
    }

    private fun fillFoundResult(foundResult: MatchResult, translation: Translation,
                                translationStb: SpannableStringBuilder): Int {
        if(foundResult.value.length > translation.translatedPhrase?.length!!*2) return 0
        translationStb.setSpan(BackgroundColorSpan(getSpanColor()), foundResult.range.first,
                foundResult.range.last + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return 1
    }

    private fun getSpanColor(): Int {
        val nightModeFlags = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return if(nightModeFlags == UI_MODE_NIGHT_YES) ContextCompat.getColor(context, R.color.dark_yellow)
        else ContextCompat.getColor(context, R.color.light_yellow)
    }

    private fun getNumberOfSpansInSent(stb: SpannableStringBuilder)
            = stb.getSpans(0, stb.length, Any::class.java)?.size

    private fun getTranslatedSentencePattern(word: String) =
            when {
                word.length > 5 -> "\\b\\S$word\\S*|\\b\\S?$word?\\S*|\\b${word.substring(0, word.length - 1)}\\b"
                word.length == 5 -> "\\b\\S$word\\S*|\\b\\S?$word?\\S*"
                else -> "\\b\\S$word\\S?[^\\s]*|\\b\\S?$word[^\\s]*"
            }

}