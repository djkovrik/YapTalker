package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed forums list in data layer.
 */
class ForumsListParsed {
  @Selector(value = "td.row4 > b > a.title")
  lateinit var titles: List<String>
  @Selector(value = "td.row4 > b > a.title", attr = "href", regex = "/forum(\\d+)/")
  lateinit var ids: List<String>
  @Selector(value = "td.row2[nowrap=nowrap]")
  lateinit var topics: List<LastTopicItemParsed>
}
