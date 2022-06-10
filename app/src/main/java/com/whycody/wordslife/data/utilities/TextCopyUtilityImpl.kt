package com.whycody.wordslife.data.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.whycody.wordslife.R

class TextCopyUtilityImpl(private val context: Context): TextCopyUtility {

    override fun copyText(view: View, text: String, anchorView: View?): Boolean {
        copyTextToClipboard(text)
        displaySnackbar(view, anchorView)
        return true
    }

    private fun copyTextToClipboard(text: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(context.getString(R.string.app_name), text)
        clipboard?.setPrimaryClip(clip)
    }

    private fun displaySnackbar(view: View, anchorView: View?) {
        val snackBar = Snackbar.make(view, context.getString(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT)
        if(anchorView != null) snackBar.anchorView = anchorView
        setMarginsOfSnackbar(snackBar, anchorView == null)
        snackBar.show()
    }

    private fun setMarginsOfSnackbar(snackbar: Snackbar, additionalMargin: Boolean) {
        val snackbarView = snackbar.view
        val margin = context.resources.getDimension(R.dimen.app_padding).toInt()
        val marginBottom = if(additionalMargin) getNavigationBarHeight() else 0
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams
        params.setMargins(margin, params.topMargin, margin,
            params.bottomMargin + marginBottom)
        snackbarView.layoutParams = params
    }

    private fun getNavigationBarHeight(): Int {
        val resources = context.resources!!
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }
}