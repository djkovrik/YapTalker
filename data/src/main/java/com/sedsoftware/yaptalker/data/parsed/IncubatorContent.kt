package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class IncubatorContent {
  @Selector(value = "[id~=news_.*]", attr = "innerHtml")
  var description: String = ""
  @Selector(value = "img[src]", attr = "src")
  var images: List<String> = emptyList()
  @Selector(value = "iframe[src]", attr = "src")
  var videos: List<String> = emptyList()
  @Selector(value = "iframe[src]", attr = "outerHtml")
  var videosRaw: List<String> = emptyList()
}
