package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class NewsHead {
  @Selector(".subtitle", defValue = "Unknown")
  lateinit var title: String
  @Selector(".subtitle", attr = "href", defValue = "")
  lateinit var link: String
  @Selector(".rating-short-value > a", format = "([-\\d]+)", defValue = "0")
  lateinit var rating: String
}
