package com.sedsoftware.yaptalker.commons.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

  var diffInSeconds = ((currentDate.time - topicDate.time) / 1000).toInt()

  val sec = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
  diffInSeconds /= 60
  val min = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
  diffInSeconds /= 60
  val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24) else diffInSeconds
  diffInSeconds /= 24
  val days = if (diffInSeconds >= 30) (diffInSeconds % 30) else diffInSeconds
  diffInSeconds /= 30
  val months = if (diffInSeconds >= 12) (diffInSeconds % 12) else diffInSeconds
  diffInSeconds /= 12
  val years = diffInSeconds

  // TODO() Adapt to Russian
  if (years > 0)
    return "$years year(s)"
  else if (months > 0)
    return "$months month(s)"
  else if (days > 0)
    return "$days day(s)"
  else if (hrs > 0)
    return "$hrs hour(s)"
  else if (min > 0) {
    if (min == 1) {
      return "minute ago"
    } else {
      return "$min min(s)"
    }
  } else {
    if (sec <= 1) {
      return "second ago"
    } else {
      return "$sec seconds"
    }
  }
}