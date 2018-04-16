package com.sedsoftware.yaptalker.presentation.extensions

import android.support.annotation.AttrRes
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar
import com.sedsoftware.yaptalker.R

fun AppCompatActivity.snackError(message: String) {
  showTopSnackbar(message, R.attr.snackColorError, R.attr.snackColorText)
}

fun AppCompatActivity.snackSuccess(message: String) {
  showTopSnackbar(message, R.attr.snackColorSuccess, R.attr.snackColorText)
}

fun AppCompatActivity.snackInfo(message: String) {
  showTopSnackbar(message, R.attr.snackColorInfo, R.attr.snackColorText)
}

fun AppCompatActivity.snackWarning(message: String) {
  showTopSnackbar(message, R.attr.snackColorWarning, R.attr.snackColorText)
}

fun AppCompatActivity.showTopSnackbar(message: String, @AttrRes bgColor: Int, @AttrRes textColor: Int) {
  TSnackbar
    .make(findViewById(R.id.content_container), message, TSnackbar.LENGTH_SHORT)
    .also { snackbar -> snackbar.view.setBackgroundColor(getColorFromAttr(bgColor)) }
    .also { snackbar ->
      (snackbar.view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as? TextView)
        ?.setTextColor(getColorFromAttr(textColor))
    }
    .show()
}
