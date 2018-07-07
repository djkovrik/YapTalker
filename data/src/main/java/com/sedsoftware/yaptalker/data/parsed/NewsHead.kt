package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class NewsHead {
    @Selector(value = ".subtitle", defValue = "Unknown")
    lateinit var title: String
    @Selector(value = ".subtitle", attr = "href", defValue = "")
    lateinit var link: String
    @Selector(value = ".rating-short-value > a", regex = "([-\\d]+)", defValue = "0")
    lateinit var rating: String
}
