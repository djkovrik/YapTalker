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

@Suppress("MagicNumber")
fun String.toMd5(): String {

  val digest = java.security.MessageDigest.getInstance("MD5")
  digest.update(this.toByteArray())
  val messageDigest = digest.digest()
  val hexString = StringBuffer()

  for (i in 0 until messageDigest.size) {
    var hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
    while (hex.length < 2)
      hex = "0" + hex
    hexString.append(hex)
  }
  return hexString.toString()
}
