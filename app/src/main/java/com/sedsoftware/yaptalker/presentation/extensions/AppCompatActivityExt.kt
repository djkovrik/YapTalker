package com.sedsoftware.yaptalker.presentation.extensions

import android.support.annotation.ColorRes
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar
import com.sedsoftware.yaptalker.R

fun AppCompatActivity.snackError(message: String) {
  showTopSnackbar(message, R.color.snackColorError, R.color.snackColorText)
}

fun AppCompatActivity.snackSuccess(message: String) {
  showTopSnackbar(message, R.color.snackColorSuccess, R.color.snackColorText)
}

fun AppCompatActivity.snackInfo(message: String) {
  showTopSnackbar(message, R.color.snackColorInfo, R.color.snackColorText)
}

fun AppCompatActivity.snackWarning(message: String) {
  showTopSnackbar(message, R.color.snackColorWarning, R.color.snackColorText)
}

fun AppCompatActivity.showTopSnackbar(message: String, @ColorRes bgColor: Int, @ColorRes textColor: Int) {
  TSnackbar
    .make(findViewById(R.id.content_container), message, TSnackbar.LENGTH_SHORT)
    .also { snackbar -> snackbar.view.setBackgroundColor(color(bgColor)) }
    .also { snackbar ->
      (snackbar.view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as? TextView)
        ?.setTextColor(color(textColor))
    }
    .show()
}