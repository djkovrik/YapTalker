package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

class MailLetterItemParsed {
  @Selector(value = "a.user-link", defValue = "")
  lateinit var authorNickname: String
  @Selector(value = "a.user-link", attr = "href", defValue = "")
  lateinit var authorLink: String
  @Selector(value = ".time-info", defValue = "")
  lateinit var dateString: String
  @Selector(value = ".new", defValue = "")
  lateinit var isNew: String
  @Selector(value = "tr[id~=r_m_*", attr = "outerHtml", format = "(\\d+)", defValue = "0")
  lateinit var letterId: String
  @Selector(value = "a[class~=(unread|replied]", attr = "href", defValue = "")
  lateinit var letterLink: String
  @Selector(value = "a[class~=(unread|replied]", defValue = "")
  lateinit var letterPreview: String
}
