package com.whycody.lyricated.data.library

import com.whycody.lyricated.data.LibraryItem

interface LibraryDao {

    fun getLibraryItems(): List<LibraryItem>
}