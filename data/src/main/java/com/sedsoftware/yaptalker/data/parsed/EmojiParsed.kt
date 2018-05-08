package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class EmojiParsed {
  @Selector(value = "td.row1 > a", defValue = "")
  lateinit var code: String
  @Selector(value = "td.row2 img", attr = "src", defValue = "")
  lateinit var link: String
}
