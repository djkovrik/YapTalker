package com.sedsoftware.yaptalker.presentation.extensions

import android.support.annotation.AttrRes
import android.support.v4.app.Fragment
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar
import com.sedsoftware.yaptalker.R

fun Fragment.snackError(message: String) {
  showTopSnackbar(message, R.attr.snackColorError, R.attr.snackColorText)
}

fun Fragment.snackSuccess(message: String) {
  showTopSnackbar(message, R.attr.snackColorSuccess, R.attr.snackColorText)
}

fun Fragment.snackInfo(message: String) {
  showTopSnackbar(message, R.attr.snackColorInfo, R.attr.snackColorText)
}

fun Fragment.snackWarning(message: String) {
  showTopSnackbar(message, R.attr.snackColorWarning, R.attr.snackColorText)
}

fun Fragment.showTopSnackbar(message: String, @AttrRes bgColor: Int, @AttrRes textColor: Int) {
  activity?.let { activity ->
    TSnackbar
      .make(activity.findViewById(R.id.content_container), message, TSnackbar.LENGTH_SHORT)
      .also { snackbar -> snackbar.view.setBackgroundColor(activity.getColorFromAttr(bgColor)) }
      .also { snackbar ->
        (snackbar.view.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as? TextView)
          ?.setTextColor(activity.getColorFromAttr(textColor))
      }
      .show()
  }
}
