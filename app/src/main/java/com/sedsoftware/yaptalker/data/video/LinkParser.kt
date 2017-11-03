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
        val regex = "(?<=oid=)([-\\d]+).*(?<=id=)([\\d]+)"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(link)

        if (matcher.find()) {
          VideoTypes.VK to "${matcher.group(1)}_${matcher.group(2)}"
        } else {
          VideoTypes.VK to ""
        }

      }

      else -> VideoTypes.OTHER to ""
    }

/**
 * Parse youtube video link to get id.
 *
 * @param link embedded video link.
 * @return video id string.
 */
fun getYoutubeVideoId(link: String): String {
  val startPosition = link.lastIndexOf("/")
  return link.substring(startPosition + 1, link.indexOf("?", startPosition))
}
