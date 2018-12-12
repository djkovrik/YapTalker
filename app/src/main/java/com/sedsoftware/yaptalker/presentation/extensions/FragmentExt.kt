package com.sedsoftware.yaptalker.presentation.extensions

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.string(@StringRes resId: Int): String =
    context?.string(resId).orEmpty()
