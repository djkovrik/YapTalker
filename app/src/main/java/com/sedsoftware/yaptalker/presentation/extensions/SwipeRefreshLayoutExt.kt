package com.sedsoftware.yaptalker.presentation.extensions

import android.support.v4.widget.SwipeRefreshLayout
import com.sedsoftware.yaptalker.R

fun SwipeRefreshLayout.setIndicatorColorScheme() {
  setColorSchemeColors(
    context.getColorFromAttr(R.attr.colorRefreshSpinner1),
    context.getColorFromAttr(R.attr.colorRefreshSpinner2),
    context.getColorFromAttr(R.attr.colorRefreshSpinner3),
    context.getColorFromAttr(R.attr.colorRefreshSpinner4)
  )
}
