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
 * Removes first and last symbols from string
 */
fun String.chopEdges() : String {
  return this.substring(1, this.length - 1)
}