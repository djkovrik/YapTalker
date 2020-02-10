package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed news page in data layer.
 */
class NewsPageParsed {
    @Selector(value = "td[class=newshead][id^=topic_]")
    var headers: List<NewsHead> = emptyList()
    @Selector(value = "td[class=postcolor news-content][id^=news_]")
    var contents: List<NewsContent> = emptyList()
    @Selector(value = "td[class=holder newsbottom]")
    var bottoms: List<NewsBottom> = emptyList()
    @Selector(value = "*", regex = "var feedOffset = (.*);", attr = "outerHtml")
    var offset: String = ""
}
