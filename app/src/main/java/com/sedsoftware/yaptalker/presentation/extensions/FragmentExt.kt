package com.sedsoftware.yaptalker.presentation.extensions

import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar
import com.sedsoftware.yaptalker.R

fun Fragment.snackError(message: String) {
  showTopSnackbar(message, R.color.snackColorError, R.color.snackColorText)
}

fun Fragment.snackSuccess(message: String) {
  showTopSnackbar(message, R.color.snackColorSuccess, R.color.snackColorText)
}

fun Fragment.snackInfo(message: String) {
  showTopSnackbar(message, R.color.snackColorInfo, R.color.snackColorText)
}

fun Fragment.snackWarning(message: String) {
  showTopSnackbar(message, R.color.snackColorWarning, R.color.snackColorText)
}

fun Fragment.showTopSnackbar(message: String, @ColorRes bgColor: Int, @ColorRes textColor: Int) {
  activity?.let { activity ->
    TSnackbar
      .make(activity.findViewById(R.id.content_container), message, TSnackbar.LENGTH_SHORT)
      .also { snackbar -> snackbar.view.setBackgroundColor(activity.color(bgColor)) }
      .also { snackbar ->
        (snackbar.view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as? TextView)
          ?.setTextColor(activity.color(textColor))
      }
      .show()
  }
}