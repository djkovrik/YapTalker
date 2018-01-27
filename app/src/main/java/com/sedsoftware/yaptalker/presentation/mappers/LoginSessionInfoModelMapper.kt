package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import com.sedsoftware.yaptalker.presentation.extensions.getLastDigits
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel
import javax.inject.Inject

/**
 * Mapper class used to transform user authorization info entity from the domain layer into YapEntity in the
 * presentation layer.
 */
class LoginSessionInfoModelMapper @Inject constructor() {

  fun transform(info: BaseEntity): YapEntity {

    info as LoginSessionInfo

    return LoginSessionInfoModel(
      nickname = info.nickname,
      userId = if (info.profileLink.isEmpty()) 0 else info.profileLink.getLastDigits(),
      title = info.title,
      uq = info.uq,
      avatar = info.avatar,
      sessionId = info.sessionId
    )
  }
}
