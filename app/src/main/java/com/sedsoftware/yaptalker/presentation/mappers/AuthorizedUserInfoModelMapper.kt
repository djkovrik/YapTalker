package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.LoginSessionInfoModel

/**
 * Mapper class used to transform user authorization info entity from the domain layer into YapEntity in the
 * presentation layer.
 */
class AuthorizedUserInfoModelMapper {

  fun transform(info: BaseEntity): YapEntity {

    info as LoginSessionInfo

    return LoginSessionInfoModel(
        nickname = info.nickname,
        title = info.title,
        uq = info.uq,
        avatar = info.avatar,
        sessionId = info.sessionId)
  }
}
