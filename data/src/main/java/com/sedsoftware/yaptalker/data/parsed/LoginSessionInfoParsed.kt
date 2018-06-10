package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed user login session info in data layer.
 */
@Suppress("VariableMinLength")
class LoginSessionInfoParsed {
  @Selector(value = "div.user-name > a", defValue = "")
  lateinit var nickname: String
  @Selector(value = "div.user-name > a", attr = "href", defValue = "")
  lateinit var profileLink: String
  @Selector(value = "div.user-title", defValue = "")
  lateinit var title: String
  @Selector(value = "span.user-rank", defValue = "0")
  lateinit var uq: String
  @Selector(value = "div[style~=float: left; padding: 10px] > a > img", attr = "src", defValue = "")
  lateinit var avatar: String
  @Selector(value = "a[href$=alpha.yaplakal.com/mail/]", regex = "\\((\\d+)\\)", defValue = "0")
  lateinit var mailCounter: String
  @Selector(value = "div[style=float: right;] > a", attr = "href", regex = "\\?key=(.*)", defValue = "")
  lateinit var sessionId: String
}
