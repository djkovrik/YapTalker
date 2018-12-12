package com.sedsoftware.yaptalker.presentation.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.visibleItemPosition(): Int {
    val layoutManager = this.layoutManager as? LinearLayoutManager
    return layoutManager?.findFirstVisibleItemPosition() ?: -1
}
