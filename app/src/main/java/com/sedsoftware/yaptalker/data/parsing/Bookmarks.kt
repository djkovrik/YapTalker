package com.sedsoftware.yaptalker.data.parsing

import pl.droidsonroids.jspoon.annotation.Selector

class Bookmarks {
  @Selector("li")
  lateinit var topics: List<BookmarkedTopic>
}

class BookmarkedTopic {
  @Selector("a[href=#]", attr = "outerHtml", format = "(\\d{2,10})", defValue = "")
  lateinit var bookmarkId: String
  @Selector("a:not([href=#])", defValue = "")
  lateinit var title: String
  @Selector("a:not([href=#])", attr = "href", defValue = "")
  lateinit var link: String
}
