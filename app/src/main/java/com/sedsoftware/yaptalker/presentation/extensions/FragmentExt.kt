package com.sedsoftware.yaptalker.presentation.extensions

import android.support.annotation.StringRes
import android.support.v4.app.Fragment

fun Fragment.string(@StringRes resId: Int): String =
    context?.string(resId) ?: ""
