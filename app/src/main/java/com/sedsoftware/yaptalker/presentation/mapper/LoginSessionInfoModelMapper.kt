package com.sedsoftware.yaptalker.presentation.mapper

import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import com.sedsoftware.yaptalker.presentation.extensions.getLastDigits
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import io.reactivex.functions.Function
import javax.inject.Inject

class LoginSessionInfoModelMapper @Inject constructor() : Function<LoginSessionInfo, LoginSessionInfoModel> {

  override fun apply(info: LoginSessionInfo): LoginSessionInfoModel =
    LoginSessionInfoModel(
      nickname = info.nickname,
      userId = if (info.profileLink.isEmpty()) 0 else info.profileLink.getLastDigits(),
      title = info.title,
      uq = info.uq,
      avatar = info.avatar,
      sessionId = info.sessionId
    )
}
