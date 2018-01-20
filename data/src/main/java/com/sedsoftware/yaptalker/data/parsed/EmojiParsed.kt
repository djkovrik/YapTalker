package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class EmojiParsed {
  @Selector("td.row1 > a", defValue = "")
  lateinit var code: String
  @Selector("td.row2 img", attr = "src", defValue = "")
  lateinit var link: String
}
