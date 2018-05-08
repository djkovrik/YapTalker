package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed forum page in data layer.
 */
class ForumPageParsed {
  @Selector(value = "a[href~=.*/forum\\d+/].title", defValue = "Unknown")
  lateinit var forumTitle: String
  @Selector(
    value = "a[href~=.*/forum\\d+/].title",
    attr = "href",
    regex = "//www.yaplakal.com/forum(\\d+)/",
    defValue = "0"
  )
  lateinit var forumId: String
  @Selector(value = "table[width=100%]")
  lateinit var navigation: ForumNavigationPanel
  @Selector(value = "table tr:has(td.row4)")
  lateinit var topics: List<TopicItemParsed>
}
