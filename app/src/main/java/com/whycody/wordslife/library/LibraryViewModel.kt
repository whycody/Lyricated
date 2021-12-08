package com.whycody.wordslife.library

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.library.LibraryDao

class LibraryViewModel(libraryDao: LibraryDao): ViewModel() {

    private val libraryItems = MutableLiveData(libraryDao.getLibraryItems())

    fun getLibraryItems() = libraryItems
}