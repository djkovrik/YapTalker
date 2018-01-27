package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class LastTopicItemParsed {
  @Selector("a.subtitle", defValue = "Unknown")
  lateinit var title: String
  @Selector("a[href~=members]", defValue = "Unknown")
  lateinit var author: String
  @Selector(".desc", format = "([0-9\\.]+ - [0-9:]+)", defValue = "Unknown")
  lateinit var date: String
}
