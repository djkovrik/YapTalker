package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class IncubatorBottom {
  @Selector(".icon-user > a", defValue = "Unknown")
  lateinit var author: String
  @Selector(".icon-user > a", attr = "href", defValue = "")
  lateinit var authorLink: String
  @Selector(".icon-date", defValue = "Unknown")
  lateinit var date: String
  @Selector(".icon-forum > a", defValue = "Unknown")
  lateinit var forumName: String
  @Selector(".icon-forum > a", attr = "href", defValue = "")
  lateinit var forumLink: String
  @Selector("span", format = "(\\d+)", defValue = "0")
  lateinit var comments: String
}
