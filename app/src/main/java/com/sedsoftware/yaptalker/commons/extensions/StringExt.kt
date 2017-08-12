package com.sedsoftware.yaptalker.commons.extensions

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