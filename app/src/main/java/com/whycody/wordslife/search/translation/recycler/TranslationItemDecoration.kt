package com.whycody.wordslife.search.translation.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.whycody.wordslife.R

class TranslationItemDecoration(private val context: Context): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val padding = context.resources.getDimension(R.dimen.app_padding)
        if (position == 0) view.setPadding(padding.toInt(), 0, 0, 0)
        else view.setPadding(0, 0, 0, 0)
    }
}