package com.sedsoftware.yaptalker.data.parsing

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed forums list in data layer.
 */
class ForumsListParsed {
  @Selector("td.row4 > b > a.title")
  lateinit var titles: List<String>
  @Selector("td.row4 > b > a.title", attr = "href", format = "/forum(\\d+)/")
  lateinit var ids: List<String>
  @Selector("td.row2[nowrap=nowrap]")
  lateinit var topics: List<LastTopicItemParsed>
}

class LastTopicItemParsed {
  @Selector("a.subtitle", defValue = "Unknown")
  lateinit var title: String
  @Selector("a[href~=members]", defValue = "Unknown")
  lateinit var author: String
  @Selector(".desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown")
  lateinit var date: String
}
