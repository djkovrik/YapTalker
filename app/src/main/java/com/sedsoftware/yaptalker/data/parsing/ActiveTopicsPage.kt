package com.sedsoftware.yaptalker.data.parsing

import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import pl.droidsonroids.jspoon.annotation.Selector

class ActiveTopicsPage() {
  @Selector("form[name=dateline]")
  lateinit var navigation: ActiveTopicsNavigationPanel
  @Selector("table tr:has(td.row4)")
  lateinit var topics: List<ActiveTopic>

  constructor(navigationPanel: ActiveTopicsNavigationPanel, topics: List<ActiveTopic>) : this() {
    this.navigation = navigationPanel
    this.topics = topics
  }
}

class ActiveTopicsNavigationPanel() : ViewType {
  @Selector(".pagelinks", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector(".pagelinks", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String

  constructor(currentPage: String, totalPages: String) : this() {
    this.currentPage = currentPage
    this.totalPages = totalPages
  }

  override fun getViewType() = ContentTypes.NAVIGATION_PANEL
}

class ActiveTopic : ViewType {
  @Selector("a.subtitle", defValue = "Unknown") lateinit var title: String
  @Selector("a.subtitle", attr = "href", defValue = "") lateinit var link: String
  @Selector("img[src*=pinned]", attr = "src", defValue = "") lateinit var isPinned: String
  @Selector("img[src*=closed]", attr = "src", defValue = "") lateinit var isClosed: String
  @Selector("td[class~=row(2|4)] > a", defValue = "Unknown") lateinit var forumTitle: String
  @Selector("td[class~=row(2|4)] > a", attr = "href", defValue = "") lateinit var forumLink: String
  @Selector("div.rating-short-value", defValue = "0") lateinit var rating: String
  @Selector("td.row4:matchesOwn(\\d+)", defValue = "0") lateinit var answers: String
  @Selector("span.desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown") lateinit var lastPostDate: String

  override fun getViewType() = ContentTypes.ACTIVE_TOPIC
}
