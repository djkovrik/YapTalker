package com.sedsoftware.yaptalker.commons.extensions

import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import es.dmoral.toasty.Toasty

fun Controller.toastError(message: String) {
  view?.let {
    Toasty.error(it.context, message, Toast.LENGTH_SHORT, true).show()
  }
}

fun Controller.toastSuccess(message: String) {
  view?.let {
    Toasty.success(it.context, message, Toast.LENGTH_SHORT, true).show()
  }
}

fun Controller.toastInfo(message: String) {
  view?.let {
    Toasty.info(it.context, message, Toast.LENGTH_SHORT, true).show()
  }
}

fun Controller.toastWarning(message: String) {
  view?.let {
    Toasty.warning(it.context, message, Toast.LENGTH_SHORT, true).show()
  }
}
