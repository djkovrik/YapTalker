package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.parsed.LoginSessionInfoParsed
import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import io.reactivex.functions.Function
import javax.inject.Inject

class LoginSessionInfoMapper @Inject constructor() : Function<LoginSessionInfoParsed, LoginSessionInfo> {

  override fun apply(from: LoginSessionInfoParsed): LoginSessionInfo =
    LoginSessionInfo(
      nickname = from.nickname,
      profileLink = from.profileLink,
      title = from.title,
      uq = from.uq.toInt(),
      avatar = from.avatar,
      sessionId = from.sessionId
    )
}
