package com.sedsoftware.yaptalker.commons.extensions

import android.support.v4.app.Fragment
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Fragment.toastError(message: String) {
  context?.let { Toasty.error(it, message, Toast.LENGTH_SHORT, true).show() }
}

fun Fragment.toastSuccess(message: String) {
  context?.let { Toasty.success(it, message, Toast.LENGTH_SHORT, true).show() }
}

fun Fragment.toastInfo(message: String) {
  context?.let { Toasty.info(it, message, Toast.LENGTH_SHORT, true).show() }
}

fun Fragment.toastWarning(message: String) {
  context?.let { Toasty.warning(it, message, Toast.LENGTH_SHORT, true).show() }
}
