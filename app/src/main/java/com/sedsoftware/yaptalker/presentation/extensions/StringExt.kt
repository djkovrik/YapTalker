package com.sedsoftware.yaptalker.presentation.extensions

import java.util.regex.Pattern

fun String.validateUrl(): String =
    when {
      startsWith("http") -> this
      else -> "http:$this"
    }

fun String.getLastDigits(): Int {

  val regex = Pattern.compile("(\\d+)(?!.*\\d)")
  val matcher = regex.matcher(this)

  if (matcher.find()) {
    return Integer.parseInt(matcher.group(1))
  }

  return 0
}
