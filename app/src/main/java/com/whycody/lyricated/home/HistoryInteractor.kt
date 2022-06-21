package com.whycody.lyricated.home

import com.whycody.lyricated.data.HistoryItem

interface HistoryInteractor {

    fun onHistoryItemClick(historyItem: HistoryItem)

    fun onStarClick(position: Int, historyItem: HistoryItem)
}