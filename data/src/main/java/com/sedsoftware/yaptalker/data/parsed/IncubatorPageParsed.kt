package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed incubator page in data layer.
 */
class IncubatorPageParsed {
    @Selector(value = "td[class=newshead][id^=topic_]")
    var headers: List<IncubatorHead> = emptyList()
    @Selector(value = "td[class=postcolor news-content][id^=news_]")
    var contents: List<IncubatorContent> = emptyList()
    @Selector(value = "td[class=holder newsbottom]")
    var bottoms: List<IncubatorBottom> = emptyList()
}
