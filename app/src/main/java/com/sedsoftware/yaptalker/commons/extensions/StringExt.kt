package com.sedsoftware.yaptalker.commons.extensions

import java.util.regex.Pattern

fun String.chopEdges() = this.substring(1, this.length - 1)

fun String.getLastDigits(): Int {

  val regex = Pattern.compile("(\\d+)(?!.*\\d)")
  val matcher = regex.matcher(this)

  if (matcher.find()) {
    return Integer.parseInt(matcher.group(1))
  }

  return 0
}

@Suppress("MagicNumber")
fun String.toMD5(): String {

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

fun String.validateURL(): String =
    when {
      startsWith("http") -> this
      else -> "http:$this"
    }

fun String.extractYapIds(): Triple<Int, Int, Int> {

  val noPageRegex = Pattern.compile("yaplakal\\.com/forum(\\d+)/topic(\\d+)\\.")
  val noPageMatcher = noPageRegex.matcher(this)

  val withPageRegex = Pattern.compile("yaplakal\\.com/forum(\\d+)/st/(\\d+)/topic(\\d+)\\.")
  val withPageMatcher = withPageRegex.matcher(this)

  val forumId: Int
  val topicId: Int
  val startingPost: Int

  return when {
    noPageMatcher.find() -> {
      forumId = noPageMatcher.group(1).toInt()
      topicId = noPageMatcher.group(2).toInt()
      Triple(forumId, topicId, 0)
    }
    withPageMatcher.find() -> {
      forumId = withPageMatcher.group(1).toInt()
      topicId = withPageMatcher.group(3).toInt()
      startingPost = withPageMatcher.group(2).toInt()
      Triple(forumId, topicId, startingPost)
    }
    else -> Triple(0, 0, 0)
  }
}
