package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class LastTopicItemParsed {
  @Selector(value = "a.subtitle", defValue = "Unknown")
  lateinit var title: String
  @Selector(value = "a[href~=members]", defValue = "Unknown")
  lateinit var author: String
  @Selector(value = ".desc", regex = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown")
  lateinit var date: String
}
