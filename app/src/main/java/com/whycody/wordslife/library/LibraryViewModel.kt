package com.whycody.wordslife.library

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whycody.wordslife.data.library.LibraryDao
import com.whycody.wordslife.library.recycler.LibraryInteractor

class LibraryViewModel(libraryDao: LibraryDao): ViewModel(), LibraryInteractor {

    private val libraryItems = MutableLiveData(libraryDao.getLibraryItems())

    fun getLibraryItems() = libraryItems

    override fun libraryItemClicked(libraryItemId: String) {
        Log.d("MOJTAG", "Clicked: $libraryItemId")
    }
}