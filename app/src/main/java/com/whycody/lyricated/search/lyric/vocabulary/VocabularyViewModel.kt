package com.whycody.lyricated.search.lyric.vocabulary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.ExtendedLyricItem
import com.whycody.lyricated.data.VocabularyItem
import java.util.*

class VocabularyViewModel: ViewModel() {

    private val vocabularyItems = MutableLiveData<List<VocabularyItem>>(emptyList())

    fun getVocabularyItems() = vocabularyItems

    fun findVocabulary(extendedLyricItem: ExtendedLyricItem) =
        vocabularyItems.postValue(getVocabularyItemsFromSentence(extendedLyricItem.mainLangSentence))

    private fun getVocabularyItemsFromSentence(sentence: String?): List<VocabularyItem> {
        if(sentence.isNullOrEmpty()) return emptyList()
        return sentence.trim().split(" ")
                .filter { it.any { character -> character.isLetter() } }
                .mapIndexed { index, s -> VocabularyItem(index, getFormattedWord(s)) }
    }

    private fun getFormattedWord(word: String) = word
            .lowercase(Locale.ROOT)
            .replace(Regex("^\\W*|\\W*\\B\$"), "")
}