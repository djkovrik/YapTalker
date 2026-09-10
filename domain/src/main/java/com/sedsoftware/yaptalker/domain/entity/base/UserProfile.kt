package com.sedsoftware.yaptalker.domain.entity.base

import com.sedsoftware.yaptalker.domain.entity.BaseEntity

class UserProfile(
    val nickname: String,
    val avatar: String,
    val photo: String,
    val group: String,
    val uq: Int,
    val signature: String,
    val registerDate: String,
    val timeZone: String,
    val birthDate: String,
    val location: String,
    val sex: String,
    val messagesCount: String
) : BaseEntity
