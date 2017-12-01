package com.sedsoftware.data.entity.mappers

import com.sedsoftware.data.entity.AuthorizedUserInfoParsed
import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.base.AuthorizedUserInfo

/**
 * Mapper class used to transform parsed user info from the data layer into YapEntity in the domain layer.
 */
class AuthorizedUserInfoMapper {

  fun transform(userInfo: AuthorizedUserInfoParsed): YapEntity =
      AuthorizedUserInfo(
          nickname = userInfo.nickname,
          title = userInfo.title,
          uq = userInfo.uq.toInt(),
          avatar = userInfo.avatar,
          sessionId = userInfo.sessionId
      )
}
