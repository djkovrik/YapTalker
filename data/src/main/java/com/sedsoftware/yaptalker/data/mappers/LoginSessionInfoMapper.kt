package com.sedsoftware.yaptalker.data.mappers

import com.sedsoftware.yaptalker.data.parsed.LoginSessionInfoParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.LoginSessionInfo
import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Mapper class used to transform parsed user info from the data layer into BaseEntity in the domain layer.
 */
class LoginSessionInfoMapper @Inject constructor() : Function<LoginSessionInfoParsed, BaseEntity> {

  override fun apply(from: LoginSessionInfoParsed): BaseEntity =
    LoginSessionInfo(
      nickname = from.nickname,
      profileLink = from.profileLink,
      title = from.title,
      uq = from.uq.toInt(),
      avatar = from.avatar,
      sessionId = from.sessionId
    )
}
