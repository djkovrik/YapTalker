package com.sedsoftware.yaptalker.data.parsing

import pl.droidsonroids.jspoon.annotation.Selector

class AuthorizedUserInfo() {
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

  constructor(nickname: String, title: String, uq: String, avatar: String, sessionId: String) : this() {
    this.nickname = nickname
    this.title = title
    this.uq = uq
    this.avatar = avatar
    this.sessionId = sessionId
  }
}
