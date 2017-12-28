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

fun String.extractYoutubeVideoId(): String {
  val startPosition = this.lastIndexOf("/")
  val endPosition = this.indexOf("?", startPosition)

  return when (endPosition) {
    -1 -> this.substring(startPosition + 1)
    else -> this.substring(startPosition + 1, endPosition)
  }
}
