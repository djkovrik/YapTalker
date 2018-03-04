package com.sedsoftware.yaptalker.presentation.extensions

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

fun RecyclerView.visibleItemPosition(): Int {
  val layoutManager = this.layoutManager as? LinearLayoutManager
  return layoutManager?.findFirstVisibleItemPosition() ?: -1
}
