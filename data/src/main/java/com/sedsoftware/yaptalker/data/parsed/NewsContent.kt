package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class NewsContent {
    @Selector(value = "[id~=news_.*]", attr = "innerHtml")
    var description: String = ""
    @Selector(value = "img[src]", attr = "src")
    var images: List<String> = emptyList()
    @Selector(value = "iframe[src]", attr = "src")
    var videos: List<String> = emptyList()
    @Selector(value = "iframe[src]", attr = "outerHtml")
    var videosRaw: List<String> = emptyList()
    @Selector(value = ".news-content", attr = "outerHtml", defValue = "", regex = "Begin Video:(.*)-->")
    var videosLinks: List<String> = emptyList()
}
