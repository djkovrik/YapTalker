package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed bookmarks page in data layer.
 */
class BookmarksParsed {
  @Selector("li")
  lateinit var topics: List<BookmarkedTopicParsed>
}

class BookmarkedTopicParsed {
  @Selector("a[href=#]", attr = "outerHtml", format = "(\\d{2,10})", defValue = "")
  lateinit var bookmarkId: String
  @Selector("a:not([href=#])", defValue = "")
  lateinit var title: String
  @Selector("a:not([href=#])", attr = "href", defValue = "")
  lateinit var link: String
}
