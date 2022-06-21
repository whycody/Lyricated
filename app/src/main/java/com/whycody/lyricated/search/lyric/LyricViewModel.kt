package com.whycody.lyricated.search.lyric

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.lyricated.data.*

class LyricViewModel: ViewModel() {

    private val currentExtendedLyricItem = MutableLiveData<ExtendedLyricItem>()

    fun setExtendedLyricItem(extendedLyricItem: ExtendedLyricItem) =
        currentExtendedLyricItem.postValue(extendedLyricItem)

    fun getCurrentExtendedLyricItem() = currentExtendedLyricItem
}