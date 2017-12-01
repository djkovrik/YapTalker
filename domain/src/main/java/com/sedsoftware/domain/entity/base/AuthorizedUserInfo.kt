package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.YapEntity
import com.sedsoftware.domain.entity.YapEntityTypes

/**
 * Class which represents authorized user info block in domain layer.
 */
class AuthorizedUserInfo(
    val nickname: String,
    val title: String,
    val uq: Int,
    val avatar: String,
    val sessionId: String
) : YapEntity {

  override fun getEntityType(): Int = YapEntityTypes.AUTHORIZED_USER_INFO_BLOCK
}
