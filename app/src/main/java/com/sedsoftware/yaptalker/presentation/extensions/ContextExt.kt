package com.sedsoftware.yaptalker.presentation.extensions

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import android.util.TypedValue

val Context.currentDensity: Int
    get() = resources.displayMetrics.density.toInt()

fun Context.color(@ColorRes colorId: Int) =
    ContextCompat.getColor(this, colorId)

fun Context.string(@StringRes resId: Int): String =
    resources.getString(resId)

fun Context.quantityString(@PluralsRes resId: Int, value: Int): String =
    resources.getQuantityString(resId, value)

fun Context.colorFromAttr(@AttrRes res: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}
