package com.sedsoftware.yaptalker.data.parsing

import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import pl.droidsonroids.jspoon.annotation.Selector

class Bookmarks {
  @Selector("li")
  lateinit var topics: List<BookmarkedTopic>
}

class BookmarkedTopic : ViewType {
  @Selector("a[href=#]", attr = "outerHtml", format = "(\\d{2,10})", defValue = "")
  lateinit var bookmarkId: String
  @Selector("a:not([href=#])", defValue = "")
  lateinit var title: String
  @Selector("a:not([href=#])", attr = "href", defValue = "")
  lateinit var link: String

  override fun getViewType() = ContentTypes.BOOKMARKED_TOPIC
}
