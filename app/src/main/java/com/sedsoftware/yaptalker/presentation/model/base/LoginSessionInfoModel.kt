package com.sedsoftware.yaptalker.presentation.model.base

import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

/**
 * Class which represents user login session info in presentation layer.
 */
class LoginSessionInfoModel(
  val nickname: String,
  val title: String,
  val uq: Int,
  val avatar: String,
  val sessionId: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.LOGIN_SESSION_INFO
}
