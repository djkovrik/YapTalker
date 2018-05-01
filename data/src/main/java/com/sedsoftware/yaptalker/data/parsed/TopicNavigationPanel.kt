package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class TopicNavigationPanel {
  @Selector(value = "td[nowrap=nowrap]:has(a[onclick~=multi_page_jump])", regex = "\\[(\\d+)\\]", defValue = "1")
  lateinit var currentPage: String
  @Selector(value = "td[nowrap=nowrap]:has(a[onclick~=multi_page_jump])", regex = "(\\d+)", defValue = "1")
  lateinit var totalPages: String
}
