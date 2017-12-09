package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

/**
 * Class which represents authorized user info block in presentation layer.
 */
class AuthorizedUserInfoModel(
    val nickname: String,
    val title: String,
    val uq: Int,
    val avatar: String,
    val sessionId: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.AUTHORIZED_USER_INFO_BLOCK
}
