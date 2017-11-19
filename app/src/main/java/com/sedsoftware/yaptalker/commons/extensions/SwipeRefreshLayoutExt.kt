package com.sedsoftware.yaptalker.commons.extensions

import android.support.v4.widget.SwipeRefreshLayout
import com.sedsoftware.yaptalker.R

fun SwipeRefreshLayout.setIndicatorColorScheme() {
  setColorSchemeColors(
      context.color(R.color.colorRefreshSpinner1),
      context.color(R.color.colorRefreshSpinner2),
      context.color(R.color.colorRefreshSpinner3),
      context.color(R.color.colorRefreshSpinner4))
}
