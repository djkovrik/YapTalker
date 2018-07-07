package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

/**
 * Class which represents user login session info in domain layer.
 */
class LoginSessionInfo(
    val nickname: String,
    val profileLink: String,
    val title: String,
    val uq: Int,
    val avatar: String,
    val mailCounter: String,
    val sessionId: String
) : BaseEntity
