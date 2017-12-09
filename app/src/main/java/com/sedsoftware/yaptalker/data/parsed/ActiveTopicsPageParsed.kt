package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed active topic page in data layer.
 */
class ActiveTopicsPageParsed {
  @Selector("form[name=dateline]")
  lateinit var navigation: ActiveTopicsNavigationPanel
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<ActiveTopicItemParsed>
}

class ActiveTopicsNavigationPanel {
  @Selector(".pagelinks", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector(".pagelinks", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String
}

class ActiveTopicItemParsed {
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
  @Selector("div.rating-short-value", defValue = "0")
  lateinit var rating: String
  @Selector("td.row4:matchesOwn(\\d+)", defValue = "0")
  lateinit var answers: String
  @Selector("span.desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown")
  lateinit var lastPostDate: String
}
