package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class SearchTopicItemParsed {
  @Selector("a.subtitle", defValue = "Unknown")
  lateinit var title: String
  @Selector("a.subtitle", attr = "href", defValue = "")
  lateinit var link: String
  @Selector("img[src*=pinned]", attr = "src", defValue = "")
  lateinit var isPinned: String
  @Selector("img[src*=closed]", attr = "src", defValue = "")
  lateinit var isClosed: String
  @Selector("td[class~=row(2|4)] > a", defValue = "Unknown")
  lateinit var forumTitle: String
  @Selector("td[class~=row(2|4)] > a", attr = "href", defValue = "")
  lateinit var forumLink: String
  @Selector("div.rating-short-value", format = "([-\\d]+)", defValue = "0")
  lateinit var rating: String
  @Selector("td.row4:matchesOwn(\\d+)", defValue = "0")
  lateinit var answers: String
  @Selector("span.desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown")
  lateinit var lastPostDate: String
}
