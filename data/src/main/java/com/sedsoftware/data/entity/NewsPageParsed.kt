package com.sedsoftware.data.entity

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

class NewsHead {
  @Selector(".subtitle", defValue = "Unknown")
  lateinit var title: String
  @Selector(".subtitle", attr = "href", defValue = "")
  lateinit var link: String
  @Selector(".rating-short-value > a", defValue = "")
  lateinit var rating: String
}

class NewsContent {
  @Selector("[id~=news_.*]", attr = "innerHtml", defValue = "")
  lateinit var description: String
  @Selector("img[src]", attr = "src")
  lateinit var images: List<String>
  @Selector("iframe[src]", attr = "src")
  lateinit var videos: List<String>
  @Selector("iframe[src]", attr = "outerHtml")
  lateinit var videosRaw: List<String>
}

class NewsBottom {
  @Selector(".icon-user > a", defValue = "Unknown")
  lateinit var author: String
  @Selector(".icon-user > a", attr = "href", defValue = "")
  lateinit var authorLink: String
  @Selector(".icon-date", defValue = "Unknown")
  lateinit var date: String
  @Selector(".icon-forum > a", defValue = "Unknown")
  lateinit var forumName: String
  @Selector(".icon-forum > a", attr = "href", defValue = "")
  lateinit var forumLink: String
  @Selector("span", defValue = "0", format = "(\\d+)")
  lateinit var comments: String
}
