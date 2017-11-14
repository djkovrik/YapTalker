package com.sedsoftware.yaptalker.data.video

import com.sedsoftware.yaptalker.data.video.Selectors.COUB_SELECTOR
import com.sedsoftware.yaptalker.data.video.Selectors.RUTUBE_SELECTOR
import com.sedsoftware.yaptalker.data.video.Selectors.VK_SELECTOR
import com.sedsoftware.yaptalker.data.video.Selectors.YAPFILES_SELECTOR
import com.sedsoftware.yaptalker.data.video.Selectors.YOUTUBE_SELECTOR
import java.util.regex.Pattern

object Selectors {
  val COUB_SELECTOR = "coub.com/embed"
  val YOUTUBE_SELECTOR = "youtube.com/embed"
  val RUTUBE_SELECTOR = "rutube.ru/video/embed"
  val YAPFILES_SELECTOR = "yapfiles.ru/get_player"
  val VK_SELECTOR = "vk.com/video_ext.php"
}

/**
 * Parses video link to get video type and id
 *
 * @param link Given video link
 * @return A pair of video type as Int and video id as String
 */
fun parseLink(link: String): Pair<Int, String> =

    when {
      link.contains(COUB_SELECTOR) ->
        VideoTypes.COUB to link.substringAfterLast("/")

      link.contains(YOUTUBE_SELECTOR) -> {
        VideoTypes.YOUTUBE to getYoutubeVideoId(link)
      }

      link.contains(RUTUBE_SELECTOR) ->
        VideoTypes.RUTUBE to link.substringAfterLast("/")

      link.contains(YAPFILES_SELECTOR) ->
        VideoTypes.YAP_FILES to link.substringAfterLast("=")

      link.contains(VK_SELECTOR) -> {
        VideoTypes.VK to getVkVideoId(link)
      }

      else -> VideoTypes.OTHER to ""
    }

fun getYoutubeVideoId(link: String): String {
  val startPosition = link.lastIndexOf("/")
  val endPosition = link.indexOf("?", startPosition)

  return when (endPosition) {
    -1 -> link.substring(startPosition + 1)
    else -> link.substring(startPosition + 1, endPosition)
  }
}

fun getVkVideoId(link: String): String {
  val regex = "(?<=oid=)([-\\d]+).*(?<=id=)([\\d]+)"
  val pattern = Pattern.compile(regex)
  val matcher = pattern.matcher(link)

  return if (matcher.find() && matcher.groupCount() == 2) {
    "${matcher.group(1)}_${matcher.group(2)}"
  } else {
    ""
  }
}
