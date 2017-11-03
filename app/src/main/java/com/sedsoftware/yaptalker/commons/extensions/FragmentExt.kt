package com.sedsoftware.yaptalker.commons.extensions

import android.support.v4.app.Fragment
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Fragment.toastError(message: String) {
  Toasty.error(context, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.toastSuccess(message: String) {
  Toasty.success(context, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.toastInfo(message: String) {
  Toasty.info(context, message, Toast.LENGTH_SHORT, true).show()
}

fun Fragment.toastWarning(message: String) {
  Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show()
}
