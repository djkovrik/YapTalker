package com.sedsoftware.yaptalker.presentation.commons.extensions

import android.view.View

fun View.hideView() {
  this.visibility = View.GONE
}

fun View.showView() {
  this.visibility = View.VISIBLE
}
