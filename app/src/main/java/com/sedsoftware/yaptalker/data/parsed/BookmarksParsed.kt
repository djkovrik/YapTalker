package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed bookmarks page in data layer.
 */
class BookmarksParsed {
  @Selector("li")
  lateinit var topics: List<BookmarkedTopicParsed>
}
