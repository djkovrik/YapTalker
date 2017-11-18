package com.sedsoftware.yaptalker.commons.extensions

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.BoolRes
import android.support.annotation.ColorRes
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue

val Context.currentDensity: Int
  get() = resources.displayMetrics.density.toInt()

fun Context.color(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.stringRes(@StringRes resId: Int): String = resources.getString(resId)

fun Context.booleanRes(@BoolRes resId: Int): Boolean = resources.getBoolean(resId)

fun Context.stringQuantityRes(@PluralsRes resId: Int, value: Int): String = resources.getQuantityString(resId, value)

fun Context.getColorFromAttr(@AttrRes res: Int): Int {
  val typedValue = TypedValue()
  theme.resolveAttribute(res, typedValue, true)
  return typedValue.data
}

