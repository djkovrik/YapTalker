package com.sedsoftware.yaptalker.presentation.commons.extensions

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun AppCompatActivity.toastError(message: String) {
  Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun AppCompatActivity.toastSuccess(message: String) {
  Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun AppCompatActivity.toastInfo(message: String) {
  Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()
}

fun AppCompatActivity.toastWarning(message: String) {
  Toasty.warning(this, message, Toast.LENGTH_SHORT, true).show()
}
