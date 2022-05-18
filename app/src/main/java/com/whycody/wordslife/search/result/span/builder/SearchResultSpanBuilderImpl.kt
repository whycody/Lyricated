package com.whycody.wordslife.search.result.span.builder

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LyricItem

class SearchResultSpanBuilderImpl(private val context: Context): SearchResultSpanBuilder {

    override fun setLyricItemSpans(lyricItem: LyricItem) {
        setSentenceSpan(lyricItem, mainSentence = true)
        setSentenceSpan(lyricItem, mainSentence = false)
    }

    private fun setSentenceSpan(lyricItem: LyricItem, mainSentence: Boolean) {
        val sentence = if(mainSentence) lyricItem.mainSentence else lyricItem.translatedSentence
        val specialCharIndexes = getSpecialCharIndexes(sentence)
        val builder = SpannableStringBuilder(sentence.replace("¦", ""))
        setSpansInBuilder(specialCharIndexes, builder, mainSentence)
        if(mainSentence) lyricItem.mainSentenceSpan = builder
        else lyricItem.translatedSentenceSpan = builder
    }

    private fun setSpansInBuilder(specialCharIndexes: List<Int>, builder: SpannableStringBuilder,
                                  mainSentence: Boolean) {
        for(i in specialCharIndexes.indices step 2) {
            builder.setSpan(getSpanStyle(mainSentence),
                specialCharIndexes[i]-i,
                specialCharIndexes[i+1]-i-1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun getSpanStyle(mainSentence: Boolean): Any {
        return if(mainSentence) StyleSpan(Typeface.BOLD)
        else BackgroundColorSpan(getSpanColor())
    }

    private fun getSpecialCharIndexes(sentence: String): List<Int> {
        val specialCharIndexes: MutableList<Int> = mutableListOf()
        var index: Int = sentence.indexOf("¦")
        while (index >= 0) {
            specialCharIndexes.add(index)
            index = sentence.indexOf("¦", index + 1)
        }
        return specialCharIndexes.toList()
    }

    private fun getSpanColor(): Int {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        val nightModeTurnedOn = currentNightMode == AppCompatDelegate.MODE_NIGHT_YES
        val lightModeTurnedOn = currentNightMode == AppCompatDelegate.MODE_NIGHT_NO
        val nightModeFlags = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return if((nightModeFlags == UI_MODE_NIGHT_YES || nightModeTurnedOn) && !lightModeTurnedOn)
            ContextCompat.getColor(context, R.color.dark_yellow)
        else ContextCompat.getColor(context, R.color.light_yellow)
    }
}