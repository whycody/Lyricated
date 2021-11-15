package com.whycody.wordslife.data.library

import com.whycody.wordslife.data.LibraryItem

interface LibraryDao {

    fun getLibraryItems(): List<LibraryItem>
}