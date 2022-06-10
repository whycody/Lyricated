package com.whycody.wordslife.data.utilities

import android.view.View

interface TextCopyUtility {
    
    fun copyText(view: View, text: String, anchorView: View? = null): Boolean
}