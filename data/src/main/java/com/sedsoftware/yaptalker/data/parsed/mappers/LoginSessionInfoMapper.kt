package com.sedsoftware.yaptalker.data.parsed.mappers

import com.sedsoftware.yaptalker.data.parsed.LoginSessionInfoParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import javax.inject.Inject

/**
 * Mapper class used to transform parsed user info from the data layer into BaseEntity in the domain layer.
 */
class LoginSessionInfoMapper @Inject constructor() {

  fun transform(userInfo: LoginSessionInfoParsed): BaseEntity =
    LoginSessionInfo(
      nickname = userInfo.nickname,
      title = userInfo.title,
      uq = userInfo.uq.toInt(),
      avatar = userInfo.avatar,
      sessionId = userInfo.sessionId
    )
}
