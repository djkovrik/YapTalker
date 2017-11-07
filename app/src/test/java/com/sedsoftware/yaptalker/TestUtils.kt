package com.sedsoftware.yaptalker

import com.sedsoftware.yaptalker.data.parsing.AuthorizedUserInfo

fun callProtectedPresenterMethod(presenter: Any, methodName: String) {
  presenter.javaClass.getDeclaredMethod(methodName).apply {
    isAccessible = true
    invoke(presenter)
  }
}

fun getDummyUserInfoAuthorized() = AuthorizedUserInfo("nickname", "title", "uq", "avatar")

fun getDummyUserInfoNotAuthorized() = AuthorizedUserInfo("", "", "", "")
