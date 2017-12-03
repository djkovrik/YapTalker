package com.sedsoftware.data.parsing.mappers

import com.sedsoftware.data.parsing.AuthorizedUserInfoParsed
import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.base.AuthorizedUserInfo

/**
 * Mapper class used to transform parsed user info from the data layer into YapEntity in the domain layer.
 */
class AuthorizedUserInfoMapper {

  fun transform(userInfo: AuthorizedUserInfoParsed): BaseEntity =
      AuthorizedUserInfo(
          nickname = userInfo.nickname,
          title = userInfo.title,
          uq = userInfo.uq.toInt(),
          avatar = userInfo.avatar,
          sessionId = userInfo.sessionId
      )
}
