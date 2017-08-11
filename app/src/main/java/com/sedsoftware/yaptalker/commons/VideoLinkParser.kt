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

fun parseLink(link: String): Pair<Int, String> {

  if (link.contains(COUB_SELECTOR)) {
    return VideoTypes.COUB to link.substringAfterLast("/")
  } else if (link.contains(YOUTUBE_SELECTOR)) {
    return VideoTypes.YOUTUBE to link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf("?"))
  } else if (link.contains(RUTUBE_SELECTOR)) {
    return VideoTypes.RUTUBE to link.substringAfterLast("/")
  } else if (link.contains(YAPFILES_SELECTOR)) {
    return VideoTypes.YAP_FILES to link.substringAfterLast("=")
  } else {
    return VideoTypes.OTHER to ""
  }
}