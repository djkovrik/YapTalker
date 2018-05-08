package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed news page in data layer.
 */
class NewsPageParsed {
  @Selector(value = "td.newshead")
  lateinit var headers: List<NewsHead>
  @Selector(value = "td.postcolor")
  lateinit var contents: List<NewsContent>
  @Selector(value = "td.holder")
  lateinit var bottoms: List<NewsBottom>
}
