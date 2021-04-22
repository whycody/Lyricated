package com.whycody.wordslife.search.lyric.vocabulary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.ExtendedLyricItem
import com.whycody.wordslife.data.VocabularyItem

class VocabularyViewModel: ViewModel() {

    private val vocabularyItems = MutableLiveData<List<VocabularyItem>>()

    fun getVocabularyItems() = vocabularyItems

    fun findVocabulary(extendedLyricItem: ExtendedLyricItem) =
        vocabularyItems.postValue(getVocabularyItemsFromSentence(extendedLyricItem.mainLangSentence))

    private fun getVocabularyItemsFromSentence(sentence: String?): List<VocabularyItem> {
        if(sentence.isNullOrEmpty()) return emptyList()
        return getFormattedWord(sentence)
            .split(" ")
            .filter { it!="-" }
            .mapIndexed { index, s -> VocabularyItem(index, s) }
    }

    private fun getFormattedWord(word: String) =
        word.toLowerCase().trim().replace(Regex("[)(:,*.?¿\\[\\]!¡\"]"), "")
}