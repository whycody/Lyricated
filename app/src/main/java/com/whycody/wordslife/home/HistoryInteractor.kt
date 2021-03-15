package com.whycody.wordslife.home

import com.whycody.wordslife.data.HistoryItem

interface HistoryInteractor {

    fun onHistoryItemClick(historyItem: HistoryItem)

    fun onStarClick(historyItem: HistoryItem)
}