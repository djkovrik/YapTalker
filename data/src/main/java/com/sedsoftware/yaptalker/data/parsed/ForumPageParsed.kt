package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed forum page in data layer.
 */
class ForumPageParsed {
  @Selector("a[href~=.*/forum\\d+/].title", defValue = "Unknown")
  lateinit var forumTitle: String
  @Selector(
      value = "a[href~=.*/forum\\d+/].title",
      attr = "href",
      format = "//www.yaplakal.com/forum(\\d+)/",
      defValue = "0")
  lateinit var forumId: String
  @Selector("table[width=100%]")
  lateinit var navigation: ForumNavigationPanel
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<TopicItemParsed>
}
