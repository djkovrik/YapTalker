package com.sedsoftware.yaptalker.presentation.mapper.util

import android.content.Context
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.quantityString
import com.sedsoftware.yaptalker.presentation.extensions.string
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateTransformer @Inject constructor(private val context: Context) {

  companion object {
    private const val MILLISECONDS_PER_SECOND = 1000
    private const val SECONDS_PER_MINUTE = 60
    private const val MINUTES_PER_HOUR = 60
    private const val HOURS_PER_DAY = 24
    private const val DAYS_PER_MONTH = 30
    private const val MONTH_PER_YEAR = 12
  }

  fun transformDateToShortView(date: String): String {
    val diff = getDifference(date)
    val calcTime = getCalculatedTime(diff)
    return buildString(calcTime)
  }

  fun transformLongToDateString(value: Long): String {
    val date = Date(value)
    val sdf = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
    return when {
      value != 0L -> sdf.format(date)
      else -> ""
    }
  }

  private fun getDifference(source: String): Int {

    val format = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
    val topicDate = format.parse(source)
    val currentDate = Calendar.getInstance().time

    return ((currentDate.time - topicDate.time) / MILLISECONDS_PER_SECOND).toInt()

  }

  private fun getCalculatedTime(diff: Int): CalculatedTime {
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

    return CalculatedTime(min, hrs, days, months, years)
  }

  private fun buildString(time: CalculatedTime): String =
    when {
      time.years > 0 -> {
        val template = context.quantityString(R.plurals.short_date_years, time.years)
        String.format(Locale.getDefault(), template, time.years)
      }
      time.months > 0 -> {
        val template = context.string(R.string.short_date_month)
        String.format(Locale.getDefault(), template, time.months)
      }
      time.days > 0 -> {
        val template = context.quantityString(R.plurals.short_date_days, time.days)
        String.format(Locale.getDefault(), template, time.days)
      }
      time.hours > 0 -> {
        val template = context.quantityString(R.plurals.short_date_hours, time.hours)
        String.format(Locale.getDefault(), template, time.hours)
      }
      time.minutes > 0 -> {
        val template = context.string(R.string.short_date_minutes)
        String.format(Locale.getDefault(), template, time.minutes)
      }
      else -> context.string(R.string.short_date_seconds_now)
    }

  private inner class CalculatedTime(
    val minutes: Int,
    val hours: Int,
    val days: Int,
    val months: Int,
    val years: Int
  )
}
