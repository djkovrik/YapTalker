package com.sedsoftware.yaptalker.commons.extensions

import android.content.Context
import android.support.annotation.BoolRes
import android.support.annotation.ColorRes
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import com.sedsoftware.yaptalker.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val MILLISEC_PER_SECOND = 1000
private const val MINUTES_PER_HOUR = 60
private const val SECONDS_PER_MINUTE = 60
private const val HOURS_PER_DAY = 24
private const val DAYS_PER_MONTH = 30
private const val MONTH_PER_YEAR = 12

fun Context.color(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)
fun Context.booleanRes(@BoolRes resId: Int) = resources.getBoolean(resId)
fun Context.stringRes(@StringRes resId: Int): String = resources.getString(resId)
fun Context.stringQuantityRes(@PluralsRes resId: Int, value: Int): String
    = resources.getQuantityString(resId, value)

/**
 * Converts date string to shortened variant.
 */
fun Context.getShortTime(source: String): String {

  val format = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
  val topicDate = format.parse(source)
  val currentDate = Calendar.getInstance().time

  val diff = ((currentDate.time - topicDate.time) / MILLISEC_PER_SECOND).toInt()

  return getSecondsText(this, diff)
}

private fun getSecondsText(context: Context, diff: Int): String {
  var diffInSeconds = diff

  // Skip seconds
  diffInSeconds /= MINUTES_PER_HOUR
  val min = if (diffInSeconds >= SECONDS_PER_MINUTE) (diffInSeconds % SECONDS_PER_MINUTE) else diffInSeconds
  diffInSeconds /= SECONDS_PER_MINUTE
  val hrs = if (diffInSeconds >= HOURS_PER_DAY) (diffInSeconds % HOURS_PER_DAY) else diffInSeconds
  diffInSeconds /= HOURS_PER_DAY
  val days = if (diffInSeconds >= DAYS_PER_MONTH) (diffInSeconds % DAYS_PER_MONTH) else diffInSeconds
  diffInSeconds /= DAYS_PER_MONTH
  val months = if (diffInSeconds >= MONTH_PER_YEAR) (diffInSeconds % MONTH_PER_YEAR) else diffInSeconds
  diffInSeconds /= MONTH_PER_YEAR
  val years = diffInSeconds

  return CalculatedTime(min, hrs, days, months, years).buildString(context)
}

private fun CalculatedTime.buildString(context: Context) =

    when {
      years > 0 -> {
        val template = context.stringQuantityRes(R.plurals.short_date_years, years)
        String.format(Locale.getDefault(), template, years)
      }
      months > 0 -> {
        val template = context.stringRes(R.string.short_date_month)
        String.format(Locale.getDefault(), template, months)
      }
      days > 0 -> {
        val template = context.stringQuantityRes(R.plurals.short_date_days, days)
        String.format(Locale.getDefault(), template, days)
      }
      hours > 0 -> {
        val template = context.stringRes(R.string.short_date_hours)
        String.format(Locale.getDefault(), template, hours)
      }
      minutes > 0 -> {
        val template = context.stringRes(R.string.short_date_minutes)
        String.format(Locale.getDefault(), template, minutes)
      }
      else -> context.stringRes(R.string.short_date_seconds_now)
    }

private class CalculatedTime(
    val minutes: Int,
    val hours: Int,
    val days: Int,
    val months: Int,
    val years: Int)
