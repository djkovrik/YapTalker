package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class ForumNavigationPanel {
  @Selector("td[nowrap=nowrap]", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector("td[nowrap=nowrap]", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String
}
