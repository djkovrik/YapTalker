package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * CClass which represents received quoted text in data layer.
 */
class QuotedPostParsed {
  @Selector("textarea[name=QPost].textinput", defValue = "")
  lateinit var quotedText: String
}
