package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class TopicTagParsed {
  @Selector("a.title:containsOwn(#)", defValue = "")
  lateinit var name: String
  @Selector("a.title:containsOwn(#)", attr = "href", defValue = "")
  lateinit var link: String
}
