package com.sedsoftware.yaptalker.commons.extensions

import java.util.regex.Pattern

/**
 * Extracts and returns id from yaplakal.com link
 */
fun String.getLastDigits(): Int {

  var value = 0

  val regex = Pattern.compile("(\\d+)(?=\\D+$)")
  val matcher = regex.matcher(this)

  if (matcher.find()) {
    value = Integer.parseInt(matcher.group(1))
  }

  return value
}

/**
 * Removes first and last symbols from string and returns result string.
 */
fun String.chopEdges() : String {
  return this.substring(1, this.length - 1)
}

/**
 * Extracts and returns date string from forum description html.
 */
fun String.extractDate() : String {
  val endPosition = this.indexOf("<br")
  return this.substring(0, endPosition)
}