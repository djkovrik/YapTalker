package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed news page in data layer.
 */
class NewsPageParsed {
  @Selector("td.newshead")
  lateinit var headers: List<NewsHead>
  @Selector("td.postcolor")
  lateinit var contents: List<NewsContent>
  @Selector("td.holder")
  lateinit var bottoms: List<NewsBottom>
}
