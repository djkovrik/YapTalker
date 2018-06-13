package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class MailInboxPageParsed {
  @Selector("tr[class~=old|new")
  lateinit var letters: List<MailLetterItemParsed>
}
