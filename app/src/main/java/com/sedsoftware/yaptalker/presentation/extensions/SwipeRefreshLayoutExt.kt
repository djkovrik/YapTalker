package com.sedsoftware.yaptalker.presentation.extensions

import android.support.v4.widget.SwipeRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.sedsoftware.yaptalker.R

fun SwipeRefreshLayout.setIndicatorColorScheme() {
    setColorSchemeColors(
        context.colorFromAttr(R.attr.colorRefreshSpinner1),
        context.colorFromAttr(R.attr.colorRefreshSpinner2),
        context.colorFromAttr(R.attr.colorRefreshSpinner3),
        context.colorFromAttr(R.attr.colorRefreshSpinner4)
    )
}

fun SwipyRefreshLayout.setIndicatorColorScheme() {
    setColorSchemeColors(
        context.colorFromAttr(R.attr.colorRefreshSpinner1),
        context.colorFromAttr(R.attr.colorRefreshSpinner2),
        context.colorFromAttr(R.attr.colorRefreshSpinner3),
        context.colorFromAttr(R.attr.colorRefreshSpinner4)
    )
}
