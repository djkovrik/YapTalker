package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class ActiveTopicsNavigationPanel {
    @Selector(value = ".pager > span.pager-current", defValue = "1")
    lateinit var currentPage: String
    @Selector(value = ".pager > a[title~=Страница]", regex = "Страница: (\\d+)", defValue = "4")
    lateinit var totalPages: String
}
