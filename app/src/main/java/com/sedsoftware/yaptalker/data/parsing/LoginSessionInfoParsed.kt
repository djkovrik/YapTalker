package com.sedsoftware.yaptalker.data.parsing

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed user login session info in data layer.
 */
class LoginSessionInfoParsed {
  @Selector("div.user-name > a", defValue = "")
  lateinit var nickname: String
  @Selector("div.user-title", defValue = "")
  lateinit var title: String
  @Selector("span.user-rank", defValue = "")
  lateinit var uq: String
  @Selector("div[style~=float: left; padding: 10px] > a > img", attr = "src", defValue = "")
  lateinit var avatar: String
  @Selector("div[style=float: right;] > a", attr = "href", format = "\\?key=(.*)", defValue = "")
  lateinit var sessionId: String
}
