package com.whycody.wordslife.data.library

import android.content.Context
import com.whycody.wordslife.R
import com.whycody.wordslife.data.LibraryItem

class LibraryDaoImpl(private val context: Context): LibraryDao {

    override fun getLibraryItems() =
        listOf(LibraryItem(HISTORY, context.getString(R.string.history), context.getString(R.string.history_desc)),
            LibraryItem(SAVED, context.getString(R.string.saved), context.getString(R.string.saved_desc)),
            LibraryItem(STUDY_MODE, context.getString(R.string.study_mode), context.getString(R.string.study_mode_desc)),
            LibraryItem(SETTINGS, context.getString(R.string.settings), context.getString(R.string.settings_desc)))

    companion object {
        const val HISTORY = "history"
        const val SAVED = "saved"
        const val STUDY_MODE = "study_mode"
        const val SETTINGS = "settings"
    }
}