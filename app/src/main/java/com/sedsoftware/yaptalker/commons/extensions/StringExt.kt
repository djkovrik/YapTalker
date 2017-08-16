package com.sedsoftware.yaptalker.commons.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

private const val MILLISEC_PER_SECOND = 1000
private const val MINUTES_PER_HOUR = 60
private const val SECONDS_PER_MINUTE = 60
private const val HOURS_PER_DAY = 24
private const val DAYS_PER_MONTH = 30
private const val MONTH_PER_YEAR = 12

/**
 * Extracts and returns id from yaplakal.com link
 */
fun String.getLastDigits(): Int {

  val regex = Pattern.compile("(\\d+)(?!.*\\d)")
  val matcher = regex.matcher(this)

  if (matcher.find()) {
    return Integer.parseInt(matcher.group(1))
  }

  return 0
}

/**
 * Removes first and last symbols from string and returns result string.
 */
fun String.chopEdges() = this.substring(1, this.length - 1)

/**
 * Extracts and returns date string from forum description html.
 */
fun String.extractDate() = this.substring(0, this.indexOf("<br"))

/**
 * Converts date string to shortened variant.
 */
fun String.getShortTime(): String {

  val format = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
  val topicDate = format.parse(this)
  val currentDate = Calendar.getInstance().time

  val diff = ((currentDate.time - topicDate.time) / MILLISEC_PER_SECOND).toInt()
  return getSecondsText(diff)
}

private fun getSecondsText(diff: Int): String {

  var diffInSeconds = diff

  val sec = if (diffInSeconds >= MINUTES_PER_HOUR) (diffInSeconds % MINUTES_PER_HOUR) else diffInSeconds
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

  return CalculatedTime(sec, min, hrs, days, months, years).buildString()
}

// TODO() Adapt to Russian
private fun CalculatedTime.buildString() =

    if (years > 0) "$years years(s)"
    else if (months > 0) "$months month(s)"
    else if (days > 0) "$days day(s)"
    else if (hours > 0) "$hours hour(s)"
    else
      if (minutes > 0) {
        if (minutes == 1) "minute ago"
        else "$minutes min(s)"
      } else {
        if (seconds <= 1) "second ago"
        else "$seconds seconds"
      }

private class CalculatedTime(
    val seconds: Int,
    val minutes: Int,
    val hours: Int,
    val days: Int,
    val months: Int,
    val years: Int)
