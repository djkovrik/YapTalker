package com.sedsoftware.yaptalker.commons.extensions

import android.content.Context
import android.support.annotation.BoolRes
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat

fun Context.booleanRes(@BoolRes resId: Int) = resources.getBoolean(resId)
fun Context.stringRes(@StringRes resId: Int): String = resources.getString(resId)
fun Context.color(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)
