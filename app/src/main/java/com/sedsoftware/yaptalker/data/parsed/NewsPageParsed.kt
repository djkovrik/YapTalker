package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed news page in data layer.
 */
class NewsPageParsed {
  @Selector(".newshead")
  lateinit var headers: List<NewsHead>
  @Selector(".news-content")
  lateinit var contents: List<NewsContent>
  @Selector(".newsbottom")
  lateinit var bottoms: List<NewsBottom>
}
