package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.AuthorizedUserInfo
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.AuthorizedUserInfoModel

/**
 * Mapper class used to transform user authorization info entity from the domain layer into YapEntity in the
 * presentation layer.
 */
class AuthorizedUserInfoModelMapper {

  fun transform(info: BaseEntity): YapEntity {

    info as AuthorizedUserInfo

    return AuthorizedUserInfoModel(
        nickname = info.nickname,
        title = info.title,
        uq = info.uq,
        avatar = info.avatar,
        sessionId = info.sessionId)
  }
}
