package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class TopicNavigationPanel {
    @Selector(value = "td.pager > span.pager-current", defValue = "1")
    lateinit var currentPage: String
    @Selector(value = "td.pager > a.page-jump", attr = "onclick", regex = "multi_page_jump\\([^,]+,\\d+,(\\d+),\\d+,.*", defValue = "1")
    lateinit var totalPages: String
}
