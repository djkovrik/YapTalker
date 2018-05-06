package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class SearchTopicsPageParsed {
  @Selector(value = "a:matchesOwn(\\d+)", defValue = "")
  lateinit var hasNextPage: String
  @Selector(value = "a[href~=searchid]", attr = "href", regex = "searchid=([\\d\\w]+)", defValue = "")
  lateinit var searchId: String
  @Selector(value = "table tr:has(td.row4)")
  lateinit var topics: List<SearchTopicItemParsed>
}
