package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class IncubatorBottom {
    @Selector(value = ".icon-user > a", defValue = "Unknown")
    lateinit var author: String
    @Selector(value = ".icon-user > a", attr = "href", defValue = "")
    lateinit var authorLink: String
    @Selector(value = ".icon-date", defValue = "Unknown")
    lateinit var date: String
    @Selector(value = ".icon-forum > a", defValue = "Unknown")
    lateinit var forumName: String
    @Selector(value = ".icon-forum > a", attr = "href", defValue = "")
    lateinit var forumLink: String
    @Selector(value = "span", regex = "(\\d+)", defValue = "0")
    lateinit var comments: String
}
