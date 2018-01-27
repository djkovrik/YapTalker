package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class NewsContent {
  @Selector("[id~=news_.*]", attr = "innerHtml", defValue = "")
  lateinit var description: String
  @Selector("img[src]", attr = "src")
  lateinit var images: List<String>
  @Selector("iframe[src]", attr = "src")
  lateinit var videos: List<String>
  @Selector("iframe[src]", attr = "outerHtml")
  lateinit var videosRaw: List<String>
}
