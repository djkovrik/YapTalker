package com.sedsoftware.yaptalker.data.model

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import timber.log.Timber

data class NewsItem(
    val summary: String,
    val forum: String,
    val topic: TopicItemList)

data class NewsItemContent(val sourceHtml: String) {

  companion object {
    private val IMAGE_SELECTOR = "img[src]"
    private val VIDEO_SELECTOR = "iframe[src]"
    private val SRC_SELECTOR = "src"
  }

  var image = ""
  var video = ""

  var text: String = Jsoup
      .clean(sourceHtml, Whitelist().addTags("i", "u", "b", "br"))

  init {
    if (text.contains("<br>")) {
      text = text.substring(0, text.indexOf("<br>"))
    }

    val content = Jsoup.parse(sourceHtml)
    val imageLinks = content.select(IMAGE_SELECTOR)
    val videoBlock = content.select(VIDEO_SELECTOR)

    image = imageLinks.attr(SRC_SELECTOR)
    video = videoBlock.toString()

    Timber.d("IMAGE: $image")
    Timber.d("VIDEO: $video")
  }
}