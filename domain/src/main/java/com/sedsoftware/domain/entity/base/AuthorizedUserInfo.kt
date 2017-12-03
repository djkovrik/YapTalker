package com.sedsoftware.domain.entity.base

import com.sedsoftware.domain.entity.BaseEntity
import com.sedsoftware.domain.entity.BaseEntityTypes

/**
 * Class which represents authorized user info block in domain layer.
 */
class AuthorizedUserInfo(
    val nickname: String,
    val title: String,
    val uq: Int,
    val avatar: String,
    val sessionId: String
) : BaseEntity {

  override fun getBaseEntityType(): Int = BaseEntityTypes.AUTHORIZED_USER_INFO
}
