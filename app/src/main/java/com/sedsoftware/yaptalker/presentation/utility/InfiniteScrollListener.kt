package com.sedsoftware.yaptalker.presentation.utility

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class InfiniteScrollListener(
  private val func: () -> Unit,
  val layoutManager: LinearLayoutManager,
  val visibleThreshold: Int = 4
) : RecyclerView.OnScrollListener() {

  private var previousTotal = 0
  private var loading = true
  private var firstVisibleItem = 0
  private var visibleItemCount = 0
  private var totalItemCount = 0

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    if (dy > 0) {
      visibleItemCount = recyclerView.childCount
      totalItemCount = layoutManager.itemCount
      firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

      if (loading) {
        if (totalItemCount > previousTotal) {
          loading = false
          previousTotal = totalItemCount
        }
      }
      if (!loading && (totalItemCount - visibleItemCount)
        <= (firstVisibleItem + visibleThreshold)) {
        func()
        loading = true
      }
    }
  }
}
