package com.sedsoftware.yaptalker.model.base

import com.sedsoftware.yaptalker.model.YapEntity
import com.sedsoftware.yaptalker.model.YapEntityTypes

class UserProfileModel(
    val nickname: String,
    val avatar: String,
    val photo: String,
    val group: String,
    val status: String,
    val uq: String,
    val signature: String,
    val rewards: String,
    val registerDate: String,
    val timeZone: String,
    val website: String,
    val birthDate: String,
    val location: String,
    val interests: String,
    val sex: String,
    val messagesCount: String,
    val messsagesPerDay: String,
    val bayans: String,
    val todayTopics: String,
    val email: String,
    val icq: String
) : YapEntity {

  override fun getBaseEntityType(): Int = YapEntityTypes.USER_PROFILE_BLOCK
}
