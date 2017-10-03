package com.sedsoftware.yaptalker.data.model

import pl.droidsonroids.jspoon.annotation.Selector

class AuthorizedUserInfo {
  @Selector("div.user-name > a", defValue = "Guest")
  lateinit var nickname: String
  @Selector("div.user-title", defValue = "")
  lateinit var title: String
  @Selector("span.user-rank", defValue = "")
  lateinit var uq: String
  @Selector("div[style~=float: left; padding: 10px] > a > img", attr = "src", defValue = "")
  lateinit var avatar: String
}
