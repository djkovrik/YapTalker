package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class ForumNavigationPanel {
    @Selector(value = "td[nowrap=nowrap]", regex = "\\[(\\d+)\\]", defValue = "0")
    lateinit var currentPage: String
    @Selector(value = "td[nowrap=nowrap]", regex = "(\\d+)", defValue = "0")
    lateinit var totalPages: String
}
