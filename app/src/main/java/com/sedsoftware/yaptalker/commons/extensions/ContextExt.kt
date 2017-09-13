package com.sedsoftware.yaptalker.commons.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat

fun Context.color(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)
fun Context.stringRes(@StringRes resId: Int): String = resources.getString(resId)
fun Context.stringQuantityRes(@PluralsRes resId: Int, value: Int): String
    = resources.getQuantityString(resId, value)

