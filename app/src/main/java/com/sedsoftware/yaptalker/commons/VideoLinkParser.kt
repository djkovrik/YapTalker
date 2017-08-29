package com.sedsoftware.yaptalker.commons

import com.sedsoftware.yaptalker.commons.Selectors.COUB_SELECTOR
import com.sedsoftware.yaptalker.commons.Selectors.RUTUBE_SELECTOR
import com.sedsoftware.yaptalker.commons.Selectors.YAPFILES_SELECTOR
import com.sedsoftware.yaptalker.commons.Selectors.YOUTUBE_SELECTOR
import com.sedsoftware.yaptalker.data.model.video.VideoTypes

object Selectors {
  val COUB_SELECTOR = "coub.com/embed"
  val YOUTUBE_SELECTOR = "youtube.com/embed"
  val RUTUBE_SELECTOR = "rutube.ru/video/embed"
  val YAPFILES_SELECTOR = "yapfiles.ru/get_player"
}

/**
 * Parses video link to get video type and id
 *
 * @param link Given video link
 * @return A pair of video type as Int and video id as String
 */
fun parseLink(link: String): Pair<Int, String> =

    when {
      link.contains(COUB_SELECTOR) -> VideoTypes.COUB to link.substringAfterLast("/")
      link.contains(YOUTUBE_SELECTOR) -> VideoTypes.YOUTUBE to link.substring(
          link.lastIndexOf("/") + 1, link.lastIndexOf("?"))
      link.contains(RUTUBE_SELECTOR) -> VideoTypes.RUTUBE to link.substringAfterLast("/")
      link.contains(YAPFILES_SELECTOR) -> VideoTypes.YAP_FILES to link.substringAfterLast("=")
      else -> VideoTypes.OTHER to ""
    }

