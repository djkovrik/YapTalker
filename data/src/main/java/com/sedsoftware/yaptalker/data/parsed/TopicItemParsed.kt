package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class TopicItemParsed {
    @Selector(value = "a.subtitle", defValue = "Unknown")
    lateinit var title: String
    @Selector(value = "a.subtitle", attr = "href", defValue = "")
    lateinit var link: String
    @Selector(value = "img[src*=pinned]", attr = "src", defValue = "")
    lateinit var isPinned: String
    @Selector(value = "img[src*=closed]", attr = "src", defValue = "")
    lateinit var isClosed: String
    @Selector(value = "td[class~=row(2|4)] > a", defValue = "Unknown")
    lateinit var author: String
    @Selector(value = "td[class~=row(2|4)] > a", attr = "href", defValue = "")
    lateinit var authorLink: String
    @Selector(value = "div.rating-short-value", regex = "([-\\d]+)", defValue = "0")
    lateinit var rating: String
    @Selector(value = "td.row4:matchesOwn(\\d+)", defValue = "0")
    lateinit var answers: String
    @Selector(value = "span.desc", regex = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown")
    lateinit var lastPostDate: String
    @Selector(value = "span.desc a ~ a", defValue = "Unknown")
    lateinit var lastPostAuthor: String
}
