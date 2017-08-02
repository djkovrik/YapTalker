package com.sedsoftware.yaptalker.commons.extensions

import android.content.Context
import android.support.annotation.BoolRes
import android.support.annotation.StringRes

fun Context.booleanRes(@BoolRes resId: Int) = resources.getBoolean(resId)
fun Context.stringRes(@StringRes resId: Int) = resources.getString(resId)