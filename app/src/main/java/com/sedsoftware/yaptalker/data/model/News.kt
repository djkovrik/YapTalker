package com.sedsoftware.yaptalker.data.model

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

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

  var images: MutableList<String> = ArrayList()
  var videos: MutableList<String> = ArrayList()

  var text: String = Jsoup
      .clean(sourceHtml, Whitelist().addTags("i", "u", "br"))
      .replace("<br>(\\s+)?\\R<br>", "<br>")

  init {
    val content = Jsoup.parse(sourceHtml)
    val imageLinks = content.select(IMAGE_SELECTOR)
    val videoLinks = content.select(VIDEO_SELECTOR)

    imageLinks.forEach {
      images.add(it.attr(SRC_SELECTOR))
    }

    videoLinks.forEach {
      videos.add(it.html())
    }
  }
}