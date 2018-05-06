package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class BookmarkedTopicParsed {
  @Selector(value = "a[href=#]", attr = "outerHtml", regex = "(\\d{2,10})", defValue = "")
  lateinit var bookmarkId: String
  @Selector(value = "a:not([href=#])", defValue = "")
  lateinit var title: String
  @Selector(value = "a:not([href=#])", attr = "href", defValue = "")
  lateinit var link: String
}
