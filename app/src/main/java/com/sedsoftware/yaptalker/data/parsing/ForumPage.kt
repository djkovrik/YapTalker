package com.sedsoftware.yaptalker.data.parsing

import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import pl.droidsonroids.jspoon.annotation.Selector

class ForumPage() {
  @Selector("a[href~=.*/forum\\d+/].title", defValue = "Unknown")
  lateinit var forumTitle: String
  @Selector("a[href~=.*/forum\\d+/].title", attr = "href", defValue = "0")
  lateinit var forumId: String
  @Selector("table[width=100%]")
  lateinit var navigation: ForumNavigationPanel
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<Topic>

  constructor(title: String, id: String, navigationPanel: ForumNavigationPanel, topicsList: List<Topic>) : this() {
    forumTitle = title
    forumId = id
    navigation = navigationPanel
    topics = topicsList
  }
}

class ForumNavigationPanel() : ViewType {
  @Selector("td[nowrap=nowrap]", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector("td[nowrap=nowrap]", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String

  constructor(currentPage: String, totalPages: String) : this() {
    this.currentPage = currentPage
    this.totalPages = totalPages
  }

  override fun getViewType() = ContentTypes.NAVIGATION_PANEL
}

class Topic : ViewType {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a.subtitle", attr = "href", defValue = "") lateinit var link: String
  @Selector("img[src*=pinned]", attr = "src", defValue = "") lateinit var isPinned: String
  @Selector("img[src*=closed]", attr = "src", defValue = "") lateinit var isClosed: String
  @Selector("td[class~=row(2|4)] > a", defValue = "Unknown") lateinit var author: String
  @Selector("td[class~=row(2|4)] > a", attr = "href") lateinit var authorLink: String
  @Selector("div.rating-short-value", defValue = "0") lateinit var rating: String
  @Selector("td.row4:matchesOwn(\\d+)", defValue = "0") lateinit var answers: String
  @Selector("span.desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown") lateinit var lastPostDate: String
  @Selector("span.desc a ~ a", defValue = "Unknown") lateinit var lastPostAuthor: String

  override fun getViewType() = ContentTypes.TOPIC
}
