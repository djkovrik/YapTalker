package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class ActiveTopicsNavigationPanel {
  @Selector(".pagelinks", format = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector(".pagelinks", format = "(\\d+)", defValue = "0")
  lateinit var totalPages: String
}
