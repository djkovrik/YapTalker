package com.sedsoftware.yaptalker.data.model

import pl.droidsonroids.jspoon.annotation.Selector

class AuthorizedUserInfo {
  @Selector("div.user-name > a", defValue = "Guest")
  lateinit var nickname: String
  @Selector("div.user-title", defValue = "")
  lateinit var title: String
  @Selector("span.user-rank", defValue = "")
  lateinit var uq: String
  @Selector("div[style~=float: left; padding: 10px] > a > img", attr = "src",
      defValue = "//www.yaplakal.com/html/avatars/noavatar.gif")
  lateinit var avatar: String

  fun getUserInfo() = UserInfo(nickname, title, uq, avatar)
}

data class UserInfo(
    val nickname: String = "Guest",
    val title: String = "",
    val uq: String = "",
    val avatar: String = "//www.yaplakal.com/html/avatars/noavatar.gif")
