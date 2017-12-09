package com.sedsoftware.yaptalker.model.mappers

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.AuthorizedUserInfo
import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.base.AuthorizedUserInfoModel

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
