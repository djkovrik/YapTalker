package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class ActiveTopicsNavigationPanel {
  @Selector(value = ".pagelinks", regex = "\\[(\\d+)\\]", defValue = "0")
  lateinit var currentPage: String
  @Selector(value = ".pagelinks", regex = "(\\d+)", defValue = "0")
  lateinit var totalPages: String
}
